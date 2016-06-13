package com.develop.zuzik.audioplayerexample.player.multiple_playback;

import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class MultiplePlaybackState {

	public final Optional<PlaybackState> playerState;
	public final boolean repeatSingle;
	public final boolean shuffle;

	public MultiplePlaybackState(Optional<PlaybackState> playerState, boolean repeatSingle, boolean shuffle) {
		this.playerState = playerState;
		this.repeatSingle = repeatSingle;
		this.shuffle = shuffle;
	}
}
