package com.develop.zuzik.audioplayerexample.player.player_states;

import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.exceptions.FailRequestAudioFocusException;
import com.develop.zuzik.audioplayerexample.player.exceptions.PlayerInitializeException;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.State;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateContext;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
class CompletedPlayerState extends BasePlayerState {

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
	protected void doOnApply(MediaPlayer player) throws IllegalStateException, PlayerInitializeException, FailRequestAudioFocusException {
		abandonAudioFocus();
	}

	@Override
	public void play() {
		super.play();
		applyState(new StartedPlayerState(this.playerStateContext));
	}

	@Override
	public void stop() {
		super.stop();
		stopPlayer();
	}
}
