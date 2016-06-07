package com.develop.zuzik.audioplayerexample.player.multiple_playback;

import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.player_initializer.PlayerInitializer;
import com.fernandocejas.arrow.optional.Optional;

import java.util.List;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class MultiplePlayerStateBundle {

	public final RepeatMode mode;
	public final Optional<PlaybackState> currentPlayerStateBundle;
	public final Optional<PlayerInitializer> currentPlayerSource;
	public final List<PlayerInitializer> allSources;

	public MultiplePlayerStateBundle(RepeatMode mode,
									 Optional<PlaybackState> currentPlayerStateBundle,
									 Optional<PlayerInitializer> currentPlayerSource,
									 List<PlayerInitializer> allSources) {
		this.mode = mode;
		this.currentPlayerStateBundle = currentPlayerStateBundle;
		this.currentPlayerSource = currentPlayerSource;
		this.allSources = allSources;
	}
}
