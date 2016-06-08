package com.develop.zuzik.audioplayerexample.player.player_states;

import com.develop.zuzik.audioplayerexample.player.playback.State;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class EndPlayerState extends BasePlayerState {

	public EndPlayerState() {
		super(false, false);
	}

	@Override
	public PlaybackState getPlaybackState() {
		return new PlaybackState(
				State.END,
				0,
				Optional.absent(),
				false);
	}
}
