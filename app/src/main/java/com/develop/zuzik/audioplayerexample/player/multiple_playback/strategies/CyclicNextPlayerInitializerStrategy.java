package com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies;

import com.develop.zuzik.audioplayerexample.player.player_initializer.PlayerInitializer;
import com.fernandocejas.arrow.optional.Optional;

import java.util.List;

/**
 * User: zuzik
 * Date: 6/13/16
 */
public class CyclicNextPlayerInitializerStrategy implements PlayerInitializerStrategy {
	@Override
	public Optional<PlayerInitializer> determine(List<PlayerInitializer> playerInitializers, PlayerInitializer currentPlayerInitializer) {
		int currentPlaybackIndex = playerInitializers.indexOf(currentPlayerInitializer);
		return currentPlaybackIndex >= 0
				? Optional.of(playerInitializers.get((currentPlaybackIndex + 1) % playerInitializers.size()))
				: Optional.absent();
	}
}
