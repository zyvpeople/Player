package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;
import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.PlaybackBundle;
import com.develop.zuzik.audioplayerexample.player.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.interfaces.PlayerStateContainer;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class PausedPlayerState extends BasePlayerState {

	public PausedPlayerState(MediaPlayer player, PlayerSource source, PlayerStateContainer stateContainer) {
		super(player, source, stateContainer);
	}

	@Override
	public PlaybackBundle getPlaybackBundle() {
		return createBundle(PlaybackState.PAUSED);
	}

	@Override
	public void setRepeat(boolean repeat) {
		super.setRepeat(repeat);
		getPlayer().setLooping(repeat);
	}

	@Override
	public void set() {
		super.set();
		onPlaybackStateChanged(PlaybackState.PAUSED);
	}

	@Override
	public void play(Context context) {
		super.play(context);
		getPlayer().start();
		setState(new StartedPlayerState(getPlayer(), getSource(), getStateContainer()));
	}

	@Override
	public void stop() {
		super.stop();
		getPlayer().stop();
		getPlayer().reset();
		setState(new IdlePlayerState(getPlayer(), getSource(), getStateContainer()));
	}

	@Override
	public void seekTo(int positionInMilliseconds) {
		super.seekTo(positionInMilliseconds);
		seekToPosition(positionInMilliseconds);
	}

	@Override
	protected void onSeekCompleted() {
		super.onSeekCompleted();
		onPlaybackStateChanged(PlaybackState.PAUSED);
	}
}
