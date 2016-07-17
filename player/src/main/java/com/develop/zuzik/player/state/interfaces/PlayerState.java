package com.develop.zuzik.player.state.interfaces;

import com.develop.zuzik.player.exception.FailRequestAudioFocusException;
import com.develop.zuzik.player.exception.PlayerInitializeException;
import com.develop.zuzik.player.state.MediaPlayerState;

/**
 * User: zuzik
 * Date: 6/2/16
 */
public interface PlayerState {

	MediaPlayerState getMediaPlayerState();

	void onRepeatChanged();

	void apply() throws IllegalStateException, PlayerInitializeException, FailRequestAudioFocusException;

	void unapply();

	void play();

	void pause();

	void stop();

	void audioFocusLossTransient();

	void audioFocusGain();

	void audioFocusLoss();

	void simulateError(Throwable throwable);

	void seekTo(int positionInMilliseconds);

	void release();
}
