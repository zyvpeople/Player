package com.develop.zuzik.musicbrowser.application.injection.component

import com.develop.zuzik.musicbrowser.application.injection.module.PlayerModule
import com.develop.zuzik.musicbrowser.presentation.fragment.playback.PlaybackListFragment
import dagger.Subcomponent
import javax.inject.Singleton

/**
 * User: zuzik
 * Date: 8/24/16
 */
@Singleton
@Subcomponent(modules = arrayOf(PlayerModule::class))
interface PlaybackListFragmentComponent {
    fun inject(fragment: PlaybackListFragment)
}