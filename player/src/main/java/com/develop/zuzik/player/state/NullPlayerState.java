package com.develop.zuzik.player.state;

import com.develop.zuzik.player.exception.PlayerInitializeException;
import com.develop.zuzik.player.interfaces.ParamAction;
import com.develop.zuzik.player.interfaces.State;
import com.develop.zuzik.player.interfaces.VideoViewSetter;
import com.develop.zuzik.player.state.interfaces.PlayerState;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class NullPlayerState implements PlayerState {

	public static final NullPlayerState INSTANCE = new NullPlayerState();

	private NullPlayerState() {
	}

	@Override
	public MediaPlayerState getMediaPlayerState() {
		return new MediaPlayerState(
				State.NONE,
				0,
				Optional.<Integer>absent());
	}

	@Override
	public void videoViewSetter(ParamAction<VideoViewSetter> success) {

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
