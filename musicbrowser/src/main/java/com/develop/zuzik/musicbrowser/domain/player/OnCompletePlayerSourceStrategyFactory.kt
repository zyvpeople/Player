package com.develop.zuzik.musicbrowser.domain.player

import com.develop.zuzik.multipleplayer.interfaces.PlayerSourceStrategyFactory
import com.develop.zuzik.multipleplayer.player_source_strategy.EndedNextPlayerSourceStrategy
import com.develop.zuzik.multipleplayer.interfaces.PlayerSourceStrategy
import com.develop.zuzik.multipleplayer.player_source_strategy.ShufflePlayerSourceStrategy

/**
 * User: zuzik
 * Date: 6/18/16
 */
class OnCompletePlayerSourceStrategyFactory<SourceInfo> : PlayerSourceStrategyFactory<SourceInfo> {
    override fun create(shuffle: Boolean): PlayerSourceStrategy<SourceInfo> {
        return if (shuffle) {
            ShufflePlayerSourceStrategy<SourceInfo>()
        } else {
            EndedNextPlayerSourceStrategy<SourceInfo>()
        }
    }
}
