package com.develop.zuzik.musicbrowser.application.injection.component

import com.develop.zuzik.musicbrowser.application.injection.module.ApplicationModule
import com.develop.zuzik.musicbrowser.application.injection.module.SettingsModule
import dagger.Component
import javax.inject.Singleton

/**
 * User: zuzik
 * Date: 8/20/16
 */
@Singleton
@Component(modules = arrayOf(
        ApplicationModule::class,
        SettingsModule::class))
interface ApplicationComponent {
    fun splashFragmentComponent(): SplashFragmentComponent
}