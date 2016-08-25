package com.develop.zuzik.musicbrowser.application.injection.component

import com.develop.zuzik.musicbrowser.application.injection.module.PlayerModule
import com.develop.zuzik.musicbrowser.domain.entity.Song
import com.develop.zuzik.musicbrowser.presentation.player.presenter_factory.MultiplePlayerActiveSourcePresenterFactory
import dagger.Subcomponent
import javax.inject.Singleton

/**
 * User: zuzik
 * Date: 8/25/16
 */
@Singleton
@Subcomponent(modules = arrayOf(PlayerModule::class))
interface PlaybackListItemViewComponent {
    fun presenterFactory(): MultiplePlayerActiveSourcePresenterFactory<Song>
}