package com.develop.zuzik.audioplayerexample.player;

import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateBundle;
import com.fernandocejas.arrow.optional.Optional;

import java.util.List;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class MultiplePlayerStateBundle {

	public final MultiplePlaybackRepeatMode mode;
	public final Optional<PlayerStateBundle> currentPlayerStateBundle;
	public final Optional<PlayerSource> currentPlayerSource;
	public final List<PlayerSource> allSources;

	public MultiplePlayerStateBundle(MultiplePlaybackRepeatMode mode,
									 Optional<PlayerStateBundle> currentPlayerStateBundle,
									 Optional<PlayerSource> currentPlayerSource,
									 List<PlayerSource> allSources) {
		this.mode = mode;
		this.currentPlayerStateBundle = currentPlayerStateBundle;
		this.currentPlayerSource = currentPlayerSource;
		this.allSources = allSources;
	}
}
