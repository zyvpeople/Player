package com.develop.zuzik.musicbrowser.presentation.player.presenter_factory

import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer
import com.develop.zuzik.multipleplayermvp.presenter.MultiplePlayerActiveSourcePresenter
import com.develop.zuzik.player.source.PlayerSource

/**
 * User: zuzik
 * Date: 8/25/16
 */
class MultiplePlayerActiveSourcePresenterFactoryImpl<SourceInfo>(private val model: MultiplePlayer.Model<SourceInfo>) : MultiplePlayerActiveSourcePresenterFactory<SourceInfo> {

    override fun create(playerSource: PlayerSource<SourceInfo>): MultiplePlayer.ActiveSourcePresenter<SourceInfo> =
            MultiplePlayerActiveSourcePresenter<SourceInfo>(this.model, playerSource)
}