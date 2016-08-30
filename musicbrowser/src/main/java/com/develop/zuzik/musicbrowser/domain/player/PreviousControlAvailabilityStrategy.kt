package com.develop.zuzik.musicbrowser.domain.player

import com.develop.zuzik.multipleplayermvp.interfaces.ControlAvailabilityStrategy
import com.develop.zuzik.player.source.PlayerSource

/**
 * User: zuzik
 * Date: 8/30/16
 */
class PreviousControlAvailabilityStrategy<SourceInfo>() : ControlAvailabilityStrategy<SourceInfo> {

    override fun available(playerSources: MutableList<PlayerSource<SourceInfo>>, currentPlayerSource: PlayerSource<SourceInfo>, shuffle: Boolean): Boolean {
        if (playerSources.isEmpty() || playerSources.size == 1) {
            return false
        }
        if (shuffle) {
            return true
        }
        val firstSong = playerSources.indexOf(currentPlayerSource) == 0
        if (firstSong) {
            return false
        }
        return true
    }
}