package com.develop.zuzik.audioplayerexample.player.player_states.interfaces;

import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.player_initializer.PlayerInitializer;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public interface PlayerStateContext {

	void setState(PlayerState state);

	void onUpdate();

	void onError(Throwable throwable);

	void requestFocus(Action success, Action fail);

	void abandonAudioFocus();

	MediaPlayer getMediaPlayer();

	PlayerInitializer getPlayerInitializer();

	boolean isRepeat();
}