package com.develop.zuzik.musicbrowser.domain.player

import com.develop.zuzik.multipleplayer.interfaces.PlayerSourceDetermineStrategy
import com.develop.zuzik.multipleplayer.interfaces.PlayerSourceDetermineStrategyFactory
import com.develop.zuzik.multipleplayer.player_source_strategy.EndedPreviousPlayerSourceDetermineStrategy
import com.develop.zuzik.multipleplayer.player_source_strategy.ShufflePlayerSourceDetermineStrategy

/**
 * User: zuzik
 * Date: 6/18/16
 */
class PreviousPlayerSourceDetermineStrategyFactory<SourceInfo> : PlayerSourceDetermineStrategyFactory<SourceInfo> {
    override fun create(shuffle: Boolean): PlayerSourceDetermineStrategy<SourceInfo> {
        return if (shuffle)
            ShufflePlayerSourceDetermineStrategy<SourceInfo>()
        else
            EndedPreviousPlayerSourceDetermineStrategy<SourceInfo>()
    }
}
