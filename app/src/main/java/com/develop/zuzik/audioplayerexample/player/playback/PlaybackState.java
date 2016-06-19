package com.develop.zuzik.audioplayerexample.player.playback;

import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 6/5/16
 */
public class PlaybackState {

	public final State state;
	public final int currentTimeInMilliseconds;
	public final Optional<Integer> maxTimeInMilliseconds;
	public final boolean repeat;
	public final PlayerSource playerSource;

	public PlaybackState(
			State state,
			int currentTimeInMilliseconds,
			Optional<Integer> maxTimeInMilliseconds,
			boolean repeat,
			PlayerSource playerSource) {
		this.state = state;
		this.currentTimeInMilliseconds = currentTimeInMilliseconds;
		this.maxTimeInMilliseconds = maxTimeInMilliseconds;
		this.repeat = repeat;
		this.playerSource = playerSource;
	}

	public PlaybackState withRepeat(boolean repeat) {
		return new PlaybackState(
				this.state,
				this.currentTimeInMilliseconds,
				this.maxTimeInMilliseconds,
				repeat,
				this.playerSource);
	}
}
