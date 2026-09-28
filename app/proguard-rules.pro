# Metadatos requeridos por reflexión, genéricos, Compose, Hilt y Room.
-keepattributes RuntimeVisibleAnnotations,RuntimeInvisibleAnnotations,AnnotationDefault,Signature,InnerClasses,EnclosingMethod

# Mantener información útil para reconstruir trazas ofuscadas sin exponer el
# nombre original de los archivos fuente.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

###############################################################################
# Room Database
###############################################################################

-keep,allowoptimization @androidx.room.Entity class * { *; }
-keep,allowoptimization @androidx.room.Database class * { *; }
-keep,allowoptimization @androidx.room.Dao interface * { *; }
-keep,allowoptimization class * extends androidx.room.RoomDatabase { *; }
-keepclassmembers,allowoptimization,includedescriptorclasses class * {
    @androidx.room.* <fields>;
    @androidx.room.* <methods>;
}
-dontwarn androidx.room.paging.**

###############################################################################
# Dagger Hilt
###############################################################################

-keep,allowoptimization @dagger.hilt.android.HiltAndroidApp class * { *; }
-keep,allowoptimization @dagger.hilt.android.AndroidEntryPoint class * { *; }
-keep,allowoptimization @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }
-keep,allowoptimization @dagger.Module class * { *; }
-keep,allowoptimization @dagger.Component interface * { *; }
-keep,allowoptimization @dagger.Subcomponent interface * { *; }
-keep,allowoptimization @dagger.hilt.DefineComponent interface * { *; }

-keepclasseswithmembers,allowoptimization,includedescriptorclasses class * {
    @javax.inject.Inject <init>(...);
}
-keepclassmembers,allowoptimization class * {
    @javax.inject.Inject <fields>;
    @javax.inject.Inject <methods>;
}

# Código generado dentro del namespace de la aplicación.
-keep,allowoptimization class **.Hilt_* { *; }
-keep,allowoptimization class **.*_HiltComponents* { *; }
-keep,allowoptimization class **.*_HiltModules* { *; }
-keep,allowoptimization class **.*_ComponentTreeDeps { *; }
-keep,allowoptimization class **.*_GeneratedInjector { *; }
-keep,allowoptimization class pe.unsch.ceis.asistencia.**_Factory { *; }
-keep,allowoptimization class pe.unsch.ceis.asistencia.**Factory { *; }

###############################################################################
# Apache POI / OOXML (.xlsx)
###############################################################################

-dontwarn org.apache.poi.**

# API común de libro, hoja, fila y celda usada por importación/exportación.
-keep,allowoptimization interface org.apache.poi.ss.usermodel.Workbook { *; }
-keep,allowoptimization interface org.apache.poi.ss.usermodel.Sheet { *; }
-keep,allowoptimization interface org.apache.poi.ss.usermodel.Row { *; }
-keep,allowoptimization interface org.apache.poi.ss.usermodel.Cell { *; }
-keep,allowoptimization class org.apache.poi.ss.usermodel.CellType { *; }
-keep,allowoptimization class org.apache.poi.ss.usermodel.DataFormatter { *; }

# Implementación XSSF concreta. Se evita mantener org.apache.poi.** completo
# para que R8 aún pueda retirar funcionalidades de POI que la app no utilice.
-keep,allowoptimization class org.apache.poi.xssf.usermodel.XSSFWorkbook { *; }
-keep,allowoptimization class org.apache.poi.xssf.usermodel.XSSFSheet { *; }
-keep,allowoptimization class org.apache.poi.xssf.usermodel.XSSFRow { *; }
-keep,allowoptimization class org.apache.poi.xssf.usermodel.XSSFCell { *; }
-keep,allowoptimization class org.apache.poi.xssf.usermodel.XSSFCellStyle { *; }

###############################################################################
# Google ML Kit Barcode Scanning (PDF417 / QR)
###############################################################################

-keep,allowoptimization class com.google.mlkit.vision.barcode.** { *; }
-keep,allowoptimization class com.google.mlkit.vision.common.** { *; }
-keep,allowoptimization class com.google.android.gms.internal.mlkit_vision_barcode.** { *; }
-keep,allowoptimization class com.google.android.gms.vision.** { *; }
-dontwarn com.google.android.gms.internal.mlkit_vision_barcode.**
-dontwarn com.google.android.gms.vision.**

###############################################################################
# Jetpack Compose y Kotlin Coroutines
###############################################################################

-keep,allowoptimization @androidx.compose.runtime.Immutable class * { *; }
-keep,allowoptimization @androidx.compose.runtime.Stable class * { *; }
-keep class kotlin.Metadata { *; }

-keep,allowoptimization interface kotlin.coroutines.Continuation { *; }
-keep,allowoptimization class * extends kotlin.coroutines.jvm.internal.BaseContinuationImpl { *; }
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory { *; }
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler { *; }
-keepclassmembers,allowoptimization class kotlinx.coroutines.** {
    volatile <fields>;
}
