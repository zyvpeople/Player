package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;
import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.State;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateContext;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class IdlePlayerState extends BasePlayerState {

	public IdlePlayerState() {
		super(true, false, player -> new PlaybackState(
				State.IDLE,
				0,
				Optional.absent(),
				player.isLooping()));
	}

	@Override
	public void apply(Context context, PlayerStateContext playerStateContext) {
		super.apply(context, playerStateContext);
		getPlayer(MediaPlayer::reset);
	}

	@Override
	public void play() {
		super.play();
		setState(new PreparingPlayerState());
	}
}
