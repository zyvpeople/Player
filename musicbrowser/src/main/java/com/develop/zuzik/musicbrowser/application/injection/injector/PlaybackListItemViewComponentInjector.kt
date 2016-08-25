package com.develop.zuzik.musicbrowser.application.injection.injector

import com.develop.zuzik.musicbrowser.application.App
import com.develop.zuzik.musicbrowser.domain.entity.Song
import com.develop.zuzik.musicbrowser.presentation.player.presenter_factory.MultiplePlayerActiveSourcePresenterFactory

/**
 * User: zuzik
 * Date: 8/25/16
 */
class PlaybackListItemViewComponentInjector {

    fun presenterFactory(): MultiplePlayerActiveSourcePresenterFactory<Song> =
            App.INSTANCE.applicationComponent.playbackListItemViewComponent().presenterFactory()
}