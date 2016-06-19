package com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies;

import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

import java.util.List;

/**
 * User: zuzik
 * Date: 6/13/16
 */
public class CyclicPreviousPlayerSourceStrategy implements PlayerSourceStrategy {
	@Override
	public Optional<PlayerSource> determine(List<PlayerSource> playerSources, PlayerSource currentPlayerSource) {
		int currentPlayerSourceIndex = playerSources.indexOf(currentPlayerSource);
		return currentPlayerSourceIndex >= 0
				? Optional.of(playerSources.get((currentPlayerSourceIndex - 1 + playerSources.size()) % playerSources.size()))
				: Optional.absent();
	}
}
