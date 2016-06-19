package com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies;

import com.develop.zuzik.audioplayerexample.player.player_initializer.PlayerInitializer;
import com.fernandocejas.arrow.optional.Optional;

import java.util.List;
import java.util.Random;

/**
 * User: zuzik
 * Date: 6/13/16
 */
public class ShufflePlayerInitializerStrategy implements PlayerInitializerStrategy {
	@Override
	public Optional<PlayerInitializer> determine(List<PlayerInitializer> playerInitializers, PlayerInitializer currentPlayerInitializer) {
		int currentPlaybackIndex = playerInitializers.indexOf(currentPlayerInitializer);
		return currentPlaybackIndex >= 0
				? Optional.of(playerInitializers.get(new Random().nextInt(Integer.MAX_VALUE) % playerInitializers.size()))
				: Optional.absent();
	}
}
