package com.develop.zuzik.musicbrowser.application.injection.injector

import com.develop.zuzik.musicbrowser.application.App
import com.develop.zuzik.musicbrowser.presentation.fragment.playback.PlaybackListFragment

/**
 * User: zuzik
 * Date: 8/24/16
 */
class PlaybackListFragmentInjector {
    fun inject(fragment: PlaybackListFragment) {
        App.INSTANCE.applicationComponent.playbackListFragmentComponent().inject(fragment)
    }
}