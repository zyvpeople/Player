package com.develop.zuzik.audioplayerexample.player.player_states.interfaces;

import com.develop.zuzik.audioplayerexample.player.exceptions.FailRequestAudioFocusException;
import com.develop.zuzik.audioplayerexample.player.exceptions.PlayerInitializeException;
import com.develop.zuzik.audioplayerexample.player.playback.MediaPlayerState;

/**
 * User: zuzik
 * Date: 6/2/16
 */
public interface PlayerState {

	MediaPlayerState getMediaPlayerState();

	void setRepeat(boolean repeat);

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
