package com.develop.zuzik.audioplayerexample.player.player_states.interfaces;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public interface PlayerStateContainer {
	void setState(PlayerState state);

	void onUpdate();

	void onError();

	void requestFocus(Action success, Action fail);

	void abandonAudioFocus();
}