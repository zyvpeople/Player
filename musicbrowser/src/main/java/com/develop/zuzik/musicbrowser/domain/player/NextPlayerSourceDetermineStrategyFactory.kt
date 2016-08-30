package com.develop.zuzik.musicbrowser.domain.player

import com.develop.zuzik.multipleplayer.interfaces.PlayerSourceDetermineStrategy
import com.develop.zuzik.multipleplayer.interfaces.PlayerSourceDetermineStrategyFactory
import com.develop.zuzik.multipleplayer.player_source_strategy.EndedNextPlayerSourceDetermineStrategy
import com.develop.zuzik.multipleplayer.player_source_strategy.ShufflePlayerSourceDetermineStrategy

/**
 * User: zuzik
 * Date: 6/18/16
 */
class NextPlayerSourceDetermineStrategyFactory<SourceInfo> : PlayerSourceDetermineStrategyFactory<SourceInfo> {
    override fun create(shuffle: Boolean): PlayerSourceDetermineStrategy<SourceInfo> {
        return if (shuffle)
            ShufflePlayerSourceDetermineStrategy<SourceInfo>()
        else
            EndedNextPlayerSourceDetermineStrategy<SourceInfo>()
    }
}
