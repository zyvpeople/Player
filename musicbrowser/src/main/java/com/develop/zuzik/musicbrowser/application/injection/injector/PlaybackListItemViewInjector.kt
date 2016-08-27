package com.develop.zuzik.musicbrowser.application.injection.injector

import com.develop.zuzik.musicbrowser.application.App
import com.develop.zuzik.musicbrowser.presentation.view.PlaybackListItemView

/**
 * User: zuzik
 * Date: 8/25/16
 */
class PlaybackListItemViewInjector {

    fun inject(view: PlaybackListItemView) =
            App.INSTANCE.applicationComponent.playbackListItemViewComponent().inject(view)
}