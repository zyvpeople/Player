package com.develop.zuzik.player.state;

import android.media.MediaPlayer;

import com.develop.zuzik.player.exception.FailRequestAudioFocusException;
import com.develop.zuzik.player.exception.PlayerInitializeException;
import com.develop.zuzik.player.interfaces.State;
import com.develop.zuzik.player.state.interfaces.PlayerStateContext;
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
