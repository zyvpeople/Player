package com.develop.zuzik.audioplayerexample.player.multiple_playback;

import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

import java.util.List;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class MultiplePlaybackState {

	public final List<PlayerSource> playerSources;
	public final Optional<PlaybackState> currentPlaybackState;
	public final boolean repeatSingle;
	public final boolean shuffle;

	public MultiplePlaybackState(
			List<PlayerSource> playerSources,
			Optional<PlaybackState> currentPlaybackState,
			boolean repeatSingle,
			boolean shuffle) {
		this.playerSources = playerSources;
		this.currentPlaybackState = currentPlaybackState;
		this.repeatSingle = repeatSingle;
		this.shuffle = shuffle;
	}

	public MultiplePlaybackState withRepeatSingle(boolean repeatSingle) {
		return new MultiplePlaybackState(
				this.playerSources,
				this.currentPlaybackState,
				repeatSingle,
				this.shuffle);
	}

	public MultiplePlaybackState withShuffle(boolean shuffle) {
		return new MultiplePlaybackState(
				this.playerSources,
				this.currentPlaybackState,
				this.repeatSingle,
				shuffle);
	}

	public MultiplePlaybackState withCurrentPlaybackState(Optional<PlaybackState> currentPlaybackState) {
		return new MultiplePlaybackState(
				this.playerSources,
				currentPlaybackState,
				this.repeatSingle,
				this.shuffle);
	}
}
