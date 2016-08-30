package com.develop.zuzik.musicbrowser.application.injection.component

import com.develop.zuzik.musicbrowser.presentation.view.PlaybackListItemView
import dagger.Subcomponent
import javax.inject.Singleton

/**
 * User: zuzik
 * Date: 8/25/16
 */
@Singleton
@Subcomponent
interface PlaybackListItemViewComponent {
    fun inject(view: PlaybackListItemView)
}