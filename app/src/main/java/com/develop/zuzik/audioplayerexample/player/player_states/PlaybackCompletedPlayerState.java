package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;
import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.PlayerStateContainer;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class PlaybackCompletedPlayerState extends PlayerState {

	public PlaybackCompletedPlayerState(MediaPlayer player, PlayerStateContainer stateContainer) {
		super(player, stateContainer);
	}

	@Override
	public void start(Context context) {
		super.start(context);
		getPlayer().start();
		getStateContainer().setState(new StartedPlayerState(getPlayer(), getStateContainer()));
	}

	@Override
	public void pause(Context context) {
		super.pause(context);
		getPlayer().pause();
		getStateContainer().setState(new PausedPlayerState(getPlayer(), getStateContainer()));
	}

	@Override
	public void stop(Context context) {
		super.stop(context);
		getPlayer().stop();
		getStateContainer().setState(new StoppedPlayerState(getPlayer(), getStateContainer()));
	}
}
