package com.develop.zuzik.musicbrowser.application.injection.component

import com.develop.zuzik.musicbrowser.presentation.fragment.splash.SplashFragment
import dagger.Subcomponent
import javax.inject.Singleton

/**
 * User: zuzik
 * Date: 8/20/16
 */
@Singleton
@Subcomponent
interface SplashFragmentComponent {
    fun inject(fragment: SplashFragment)
}