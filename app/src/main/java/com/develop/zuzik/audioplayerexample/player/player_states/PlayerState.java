package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;
import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.PlayerStateContainer;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public abstract class PlayerState {
	private final MediaPlayer player;
	private final PlayerStateContainer stateContainer;

	public PlayerState(MediaPlayer player, PlayerStateContainer stateContainer) {
		this.player = player;
		this.stateContainer = stateContainer;
	}

	protected final MediaPlayer getPlayer() {
		return this.player;
	}

	protected final PlayerStateContainer getStateContainer() {
		return this.stateContainer;
	}

	public void start(Context context) {
	}

	public void pause(Context context) {
	}

	public void stop(Context context) {
	}
}
