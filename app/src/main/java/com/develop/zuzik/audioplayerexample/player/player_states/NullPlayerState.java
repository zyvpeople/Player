package com.develop.zuzik.audioplayerexample.player.player_states;

import com.develop.zuzik.audioplayerexample.player.exceptions.PlayerInitializeException;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.State;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerState;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class NullPlayerState implements PlayerState {

	@Override
	public MediaPlayerState getMediaPlayerState() {
		return new MediaPlayerState(
				State.NONE,
				0,
				Optional.absent());
	}

	@Override
	public void onRepeatChanged() {

	}

	@Override
	public void apply() throws IllegalStateException, PlayerInitializeException {

	}

	@Override
	public void unapply() {

	}

	@Override
	public void play() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void stop() {

	}

	@Override
	public void audioFocusLossTransient() {

	}

	@Override
	public void audioFocusGain() {

	}

	@Override
	public void audioFocusLoss() {

	}

	@Override
	public void simulateError(Throwable throwable) {

	}

	@Override
	public void seekTo(int positionInMilliseconds) {

	}

	@Override
	public void release() {

	}
}
