package com.develop.zuzik.musicbrowser.application.injection.module

import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlaybackSettings
import com.develop.zuzik.multipleplayermvp.settings.InMemoryMultiplePlaybackSettings
import com.develop.zuzik.musicbrowser.application.settings.SplashSettings
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * User: zuzik
 * Date: 8/20/16
 */
@Singleton
@Module
class SettingsModule {
    @Provides
    @Singleton
    fun splashSettings(): SplashSettings = SplashSettings(3000)

    @Provides
    @Singleton
    fun playbackSettings(): MultiplePlaybackSettings = InMemoryMultiplePlaybackSettings()
}