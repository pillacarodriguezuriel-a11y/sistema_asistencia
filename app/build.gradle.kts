import java.util.Properties
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

abstract class VerifyReleaseArtifactsTask : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val apkDirectory: DirectoryProperty

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val mappingDirectory: DirectoryProperty

    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val proguardRules: RegularFileProperty

    @get:Input
    abstract val maximumApkSizeBytes: Property<Long>

    @TaskAction
    fun verify() {
        val apkFiles = apkDirectory.asFile.get()
            .listFiles { file -> file.isFile && file.extension == "apk" }
            .orEmpty()
            .sortedBy { it.name }
        check(apkFiles.isNotEmpty()) {
            "No se encontró una APK release en ${apkDirectory.asFile.get()}."
        }

        val maximumBytes = maximumApkSizeBytes.get()
        apkFiles.forEach { apk ->
            check(apk.length() < maximumBytes) {
                "${apk.name} pesa ${apk.length()} bytes y supera la meta de $maximumBytes bytes."
            }
            val sizeMiB = apk.length().toDouble() / (1024 * 1024)
            logger.lifecycle("${apk.name}: %.2f MiB (meta: < 15 MiB)".format(sizeMiB))
        }

        val mappingFile = mappingDirectory.file("mapping.txt").get().asFile
        val configurationFile = mappingDirectory.file("configuration.txt").get().asFile
        check(mappingFile.isFile && mappingFile.length() > 0L) {
            "R8 no generó mapping.txt; no sería posible reconstruir trazas ofuscadas."
        }
        check(configurationFile.isFile && configurationFile.length() > 0L) {
            "R8 no generó configuration.txt para auditar las reglas aplicadas."
        }

        val mappingText = mappingFile.readText()
        val preservedClassMappings = listOf(
            "pe.unsch.ceis.asistencia.Hilt_MainActivity -> " +
                "pe.unsch.ceis.asistencia.Hilt_MainActivity:",
            "pe.unsch.ceis.asistencia.ui.base.BaseViewModel -> " +
                "pe.unsch.ceis.asistencia.ui.base.BaseViewModel:",
            "pe.unsch.ceis.asistencia.ui.base.BaseViewModel_Factory -> " +
                "pe.unsch.ceis.asistencia.ui.base.BaseViewModel_Factory:",
        )
        val missingClassMappings = preservedClassMappings.filterNot(mappingText::contains)
        check(missingClassMappings.isEmpty()) {
            "R8 renombró o retiró clases críticas de Hilt/ViewModel: " +
                missingClassMappings.joinToString()
        }

        val rulesText = proguardRules.asFile.get().readText()
        val criticalRules = mapOf(
            "anotaciones" to "RuntimeVisibleAnnotations",
            "Room" to "@androidx.room.Entity",
            "Hilt" to "@dagger.hilt.android.lifecycle.HiltViewModel",
            "Apache POI" to "org.apache.poi.xssf.usermodel.XSSFWorkbook",
            "ML Kit" to "com.google.mlkit.vision.barcode.",
            "Compose" to "@androidx.compose.runtime.Immutable",
            "coroutines" to "kotlin.coroutines.jvm.internal.BaseContinuationImpl",
        )
        val missingRules = criticalRules.filterValues { token -> token !in rulesText }.keys
        check(missingRules.isEmpty()) {
            "Faltan reglas R8 críticas para: ${missingRules.joinToString()}."
        }

        logger.lifecycle(
            "R8 verificado: mapping.txt y configuration.txt disponibles; reglas críticas presentes.",
        )
    }
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties().apply {
    if (keystorePropertiesFile.isFile) {
        keystorePropertiesFile.inputStream().use(::load)
    }
}

val signingKeys = listOf("storeFile", "storePassword", "keyAlias", "keyPassword")
val releaseKeystoreFile = keystoreProperties.getProperty("storeFile")?.let(rootProject::file)
val releaseSigningConfigured =
    signingKeys.all { !keystoreProperties.getProperty(it).isNullOrBlank() } &&
        releaseKeystoreFile?.isFile == true

android {
    namespace = "pe.unsch.ceis.asistencia"
    compileSdk = 35

    defaultConfig {
        applicationId = "pe.unsch.ceis.asistencia"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        if (releaseSigningConfigured) {
            create("release") {
                storeFile = requireNotNull(releaseKeystoreFile)
                storePassword = keystoreProperties.getProperty("storePassword")
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )

            if (releaseSigningConfigured) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

hilt {
    enableAggregatingTask = true
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2025.05.01")

    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.activity:activity-compose:1.10.1")

    implementation(composeBom)
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")

    implementation("com.google.dagger:hilt-android:2.57.1")
    ksp("com.google.dagger:hilt-compiler:2.57.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation("androidx.navigation:navigation-compose:2.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")

    androidTestImplementation(composeBom)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}

val verifyDomainPurity by tasks.registering {
    group = "verification"
    description = "Verifica que Domain no dependa de Android ni de capas externas."
    inputs.dir(
        layout.projectDirectory.dir(
            "src/main/kotlin/pe/unsch/ceis/asistencia/domain",
        ),
    )

    doLast {
        val forbiddenImportPrefixes = listOf(
            "import android.",
            "import androidx.",
            "import dagger.",
            "import javax.inject.",
            "import pe.unsch.ceis.asistencia.data.",
            "import pe.unsch.ceis.asistencia.di.",
            "import pe.unsch.ceis.asistencia.ui.",
        )
        val violations = inputs.files.files
            .asSequence()
            .filter { it.isFile && it.extension == "kt" }
            .flatMap { source ->
                source.readLines().asSequence().mapIndexedNotNull { index, line ->
                    val normalized = line.trim()
                    val forbidden = forbiddenImportPrefixes.any(normalized::startsWith)
                    if (forbidden) {
                        "${source.path}:${index + 1}: $normalized"
                    } else {
                        null
                    }
                }
            }
            .toList()

        check(violations.isEmpty()) {
            "Domain contiene dependencias prohibidas:\n${violations.joinToString("\n")}"
        }
    }
}

val verifyLayerBoundaries by tasks.registering {
    group = "verification"
    description = "Verifica que UI, Data y Core respeten la dirección de dependencias."
    inputs.dir(
        layout.projectDirectory.dir(
            "src/main/kotlin/pe/unsch/ceis/asistencia",
        ),
    )

    doLast {
        val forbiddenByLayer = mapOf(
            "/ui/" to listOf(
                "import pe.unsch.ceis.asistencia.data.",
                "import pe.unsch.ceis.asistencia.di.",
            ),
            "/data/" to listOf(
                "import pe.unsch.ceis.asistencia.ui.",
                "import pe.unsch.ceis.asistencia.di.",
            ),
            "/core/" to listOf(
                "import pe.unsch.ceis.asistencia.data.",
                "import pe.unsch.ceis.asistencia.di.",
                "import pe.unsch.ceis.asistencia.domain.",
                "import pe.unsch.ceis.asistencia.ui.",
            ),
        )
        val violations = inputs.files.files
            .asSequence()
            .filter { it.isFile && it.extension == "kt" }
            .flatMap { source ->
                val normalizedPath = source.invariantSeparatorsPath
                val forbiddenImports = forbiddenByLayer.entries
                    .firstOrNull { (pathSegment, _) -> pathSegment in normalizedPath }
                    ?.value
                    .orEmpty()

                source.readLines().asSequence().mapIndexedNotNull { index, line ->
                    val normalizedLine = line.trim()
                    if (forbiddenImports.any(normalizedLine::startsWith)) {
                        "${source.path}:${index + 1}: $normalizedLine"
                    } else {
                        null
                    }
                }
            }
            .toList()

        check(violations.isEmpty()) {
            "Se detectaron dependencias de capa prohibidas:\n${violations.joinToString("\n")}"
        }
    }
}

tasks.matching { it.name == "check" || it.name == "testDebugUnitTest" }.configureEach {
    dependsOn(verifyDomainPurity, verifyLayerBoundaries)
}

val verifyReleaseApkSize by tasks.registering(VerifyReleaseArtifactsTask::class) {
    group = "verification"
    description = "Comprueba que la APK release sea menor a 15 MiB y que R8 genere sus metadatos."
    apkDirectory.set(layout.buildDirectory.dir("outputs/apk/release"))
    mappingDirectory.set(layout.buildDirectory.dir("outputs/mapping/release"))
    proguardRules.set(layout.projectDirectory.file("proguard-rules.pro"))
    maximumApkSizeBytes.set(15L * 1024L * 1024L)
}

tasks.matching { it.name == "assembleRelease" }.configureEach {
    finalizedBy(verifyReleaseApkSize)
}
