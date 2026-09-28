package pe.unsch.ceis.asistencia.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pe.unsch.ceis.asistencia.ui.util.AudioHapticHelper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UiModule {
    @Provides
    @Singleton
    fun provideAudioHapticHelper(
        @ApplicationContext context: Context,
    ): AudioHapticHelper = AudioHapticHelper(context)
}
