package com.develop.zuzik.musicbrowser.presentation.player.presenter_factory

import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer
import com.develop.zuzik.player.source.PlayerSource

/**
 * User: zuzik
 * Date: 8/25/16
 */
interface MultiplePlayerActiveSourcePresenterFactory<SourceInfo> {
    fun create(playerSource: PlayerSource<SourceInfo>): MultiplePlayer.ActiveSourcePresenter<SourceInfo>
}