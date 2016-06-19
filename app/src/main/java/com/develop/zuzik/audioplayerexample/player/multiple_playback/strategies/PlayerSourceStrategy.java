package com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies;

import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

import java.util.List;

/**
 * User: zuzik
 * Date: 6/13/16
 */
public interface PlayerSourceStrategy {
	Optional<PlayerSource> determine(
			List<PlayerSource> playerSources,
			PlayerSource currentPlayerSource);
}
