package com.develop.zuzik.audioplayerexample.player.player_states.interfaces;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;

/**
 * User: zuzik
 * Date: 6/2/16
 */
public interface PlayerState {

	PlaybackState getPlaybackState();

	void setRepeat(boolean repeat);

	void apply();

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
