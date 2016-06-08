package com.develop.zuzik.audioplayerexample.player.player_states;

import com.develop.zuzik.audioplayerexample.player.playback.State;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class IdlePlayerState extends BasePlayerState {

	public IdlePlayerState() {
		super(true, false);
	}

	@Override
	public PlaybackState getPlaybackState() {
		return new PlaybackState(
				State.IDLE,
				0,
				Optional.absent(),
				getPlayer().isLooping());
	}

	@Override
	public void play() {
		super.play();
		getPlayer().reset();
		setState(new PreparingPlayerState());
	}
}
