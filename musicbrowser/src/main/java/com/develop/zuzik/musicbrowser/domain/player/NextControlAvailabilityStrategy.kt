package com.develop.zuzik.musicbrowser.domain.player

import com.develop.zuzik.multipleplayermvp.interfaces.ControlAvailabilityStrategy
import com.develop.zuzik.player.source.PlayerSource

/**
 * User: zuzik
 * Date: 8/30/16
 */
class NextControlAvailabilityStrategy<SourceInfo>() : ControlAvailabilityStrategy<SourceInfo> {

    override fun available(playerSources: MutableList<PlayerSource<SourceInfo>>, currentPlayerSource: PlayerSource<SourceInfo>, shuffle: Boolean): Boolean {
        if (playerSources.isEmpty() || playerSources.size == 1) {
            return false
        }
        if (shuffle) {
            return true
        }
        val lastSong = playerSources.indexOf(currentPlayerSource) == playerSources.size - 1
        if (lastSong) {
            return false
        }
        return true
    }
}