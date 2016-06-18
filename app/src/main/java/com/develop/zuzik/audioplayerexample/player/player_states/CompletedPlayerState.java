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
public class CompletedPlayerState extends BasePlayerState {

	public CompletedPlayerState(PlayerStateContext playerStateContext) {
		super(playerStateContext, true, true);
	}

	@Override
	protected MediaPlayerState playerToState(MediaPlayer player) {
		int maxDuration = player.getDuration();
		return new MediaPlayerState(
				State.COMPLETED,
				maxDuration,
				Optional.of(maxDuration));
	}

	@Override
	public void apply() {
		super.apply();
		abandonAudioFocus();
		getPlayer(value -> setMediaPlayerState(playerToState(value)));
	}

	@Override
	public void play() {
		super.play();
		setState(new StartedPlayerState(this.playerStateContext));
	}

	@Override
	public void stop() {
		super.stop();
		stopPlayer();
	}
}
