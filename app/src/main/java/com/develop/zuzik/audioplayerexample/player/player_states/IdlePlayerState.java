package com.develop.zuzik.audioplayerexample.player.player_states;

import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.playback.MediaPlayerState;
import com.develop.zuzik.audioplayerexample.player.playback.State;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateContext;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class IdlePlayerState extends BasePlayerState {

	public IdlePlayerState(PlayerStateContext playerStateContext) {
		super(playerStateContext, true, false);
	}

	@Override
	protected MediaPlayerState playerToState(MediaPlayer player) {
		return new MediaPlayerState(
				State.IDLE,
				0,
				Optional.absent());
	}

	@Override
	public void apply() {
		super.apply();
		getPlayer(MediaPlayer::reset);
		getPlayer(value -> setMediaPlayerState(playerToState(value)));
	}

	@Override
	public void play() {
		super.play();
		setState(new PreparingPlayerState(this.playerStateContext));
	}
}
