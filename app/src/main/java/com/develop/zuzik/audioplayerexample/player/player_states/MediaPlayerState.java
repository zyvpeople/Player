package com.develop.zuzik.audioplayerexample.player.player_states;

import com.develop.zuzik.audioplayerexample.player.playback.interfaces.State;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 6/19/16
 */
public class MediaPlayerState {
	public final State state;
	public final int currentTimeInMilliseconds;
	public final Optional<Integer> maxTimeInMilliseconds;

	public MediaPlayerState(
			State state,
			int currentTimeInMilliseconds,
			Optional<Integer> maxTimeInMilliseconds) {
		this.state = state;
		this.currentTimeInMilliseconds = currentTimeInMilliseconds;
		this.maxTimeInMilliseconds = maxTimeInMilliseconds;
	}
}
