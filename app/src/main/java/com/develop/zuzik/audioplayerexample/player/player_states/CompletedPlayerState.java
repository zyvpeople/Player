package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;
import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.PlaybackBundle;
import com.develop.zuzik.audioplayerexample.player.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.PlayerStateContainer;
import com.develop.zuzik.audioplayerexample.player.player_initializer.PlayerInitializer;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class CompletedPlayerState extends BasePlayerState {

	public CompletedPlayerState(MediaPlayer player, PlayerInitializer initializer, PlayerStateContainer stateContainer) {
		super(player, initializer, stateContainer);
	}

	@Override
	public void set() {
		super.set();
		int maxDuration = getPlayer().getDuration();
		onPlaybackStateChanged(new PlaybackBundle(PlaybackState.COMPLETED, maxDuration, maxDuration));
	}

	@Override
	public void play(Context context) {
		super.play(context);
		getPlayer().start();
		setState(new StartedPlayerState(getPlayer(), getInitializer(), getStateContainer()));
	}

	@Override
	public void seekTo(int positionInMilliseconds) {
		super.seekTo(positionInMilliseconds);
		seekToPosition(positionInMilliseconds);
	}
}
