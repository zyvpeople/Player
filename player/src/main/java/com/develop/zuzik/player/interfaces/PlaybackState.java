package com.develop.zuzik.player.interfaces;

import com.develop.zuzik.player.source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

import java.io.Serializable;

/**
 * User: zuzik
 * Date: 6/5/16
 */
public class PlaybackState<SourceInfo> implements Serializable {

	public final State state;
	public final int currentTimeInMilliseconds;
	public final Optional<Integer> maxTimeInMilliseconds;
	public final boolean repeat;
	public final PlayerSource<SourceInfo> playerSource;

	public PlaybackState(
			State state,
			int currentTimeInMilliseconds,
			Optional<Integer> maxTimeInMilliseconds,
			boolean repeat,
			PlayerSource<SourceInfo> playerSource) {
		this.state = state;
		this.currentTimeInMilliseconds = currentTimeInMilliseconds;
		this.maxTimeInMilliseconds = maxTimeInMilliseconds;
		this.repeat = repeat;
		this.playerSource = playerSource;
	}

	public PlaybackState<SourceInfo> withRepeat(boolean repeat) {
		return new PlaybackState<>(
				this.state,
				this.currentTimeInMilliseconds,
				this.maxTimeInMilliseconds,
				repeat,
				this.playerSource);
	}
}
