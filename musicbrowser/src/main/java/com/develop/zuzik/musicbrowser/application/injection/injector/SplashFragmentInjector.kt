package com.develop.zuzik.musicbrowser.application.injection.injector

import com.develop.zuzik.musicbrowser.application.App
import com.develop.zuzik.musicbrowser.presentation.fragment.splash.SplashFragment

/**
 * User: zuzik
 * Date: 8/20/16
 */
class SplashFragmentInjector {
    fun inject(fragment: SplashFragment) = App.INSTANCE.applicationComponent.splashFragmentComponent().inject(fragment)
}