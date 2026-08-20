package pe.unsch.ceis.asistencia.di

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /**
     * Hilt ya expone el binding [ApplicationContext]. Este provider lo consume
     * para entregar recursos con alcance de aplicación sin duplicar el binding
     * interno de `Context` que Hilt genera automáticamente.
     */
    @Provides
    @Singleton
    fun provideApplicationResources(
        @ApplicationContext applicationContext: Context,
    ): Resources = applicationContext.resources
}

