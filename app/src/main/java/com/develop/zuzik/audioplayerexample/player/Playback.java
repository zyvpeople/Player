package com.develop.zuzik.audioplayerexample.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.develop.zuzik.audioplayerexample.player.interfaces.PlaybackListener;
import com.develop.zuzik.audioplayerexample.player.interfaces.PlayerStateContainer;
import com.develop.zuzik.audioplayerexample.player.null_objects.NullPlaybackListener;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.develop.zuzik.audioplayerexample.player.player_states.IdlePlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.NullPlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.PlayerState;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class Playback implements PlayerStateContainer {

	private PlayerState state = new NullPlayerState();
	public final PlayerSource source;
	private PlaybackListener playbackListener = new NullPlaybackListener();
	private boolean repeat;

	public Playback(PlayerSource source) {
		this.source = source;
	}

	//region Getters/Setters

	public void setPlaybackListener(PlaybackListener playbackListener) {
		this.playbackListener = playbackListener != null
				? playbackListener
				: new NullPlaybackListener();
		this.state.setPlaybackListener(this.playbackListener);
	}

	public PlayerStateBundle getPlayerStateBundle() {
		return this.state.getPlayerStateBundle();
	}

	//TODO: create 2 methods
	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
		this.state.setRepeat(this.repeat);
	}

	//endregion

	//region Play

	public void init() {
		setState(new IdlePlayerState(new MediaPlayer(), this.source, this));
	}

	public void release() {
		this.state.release();
	}

	public void play(Context context) {
		this.state.play(context);
	}

	public void pause() {
		this.state.pause();
	}

	public void stop() {
		this.state.stop();
	}

	public void seekTo(int positionInMilliseconds) {
		this.state.seekTo(positionInMilliseconds);
	}

	//endregion

	//region PlayerStateContainer

	@Override
	public void setState(PlayerState state) {
		logState(this.state, state);
		this.state.unset();
		this.state.setPlaybackListener(null);
		this.state = state;
		this.state.setPlaybackListener(this.playbackListener);
		this.state.setRepeat(this.repeat);
		this.state.set();
	}

	private void logState(PlayerState oldState, PlayerState newState) {
		Log.i(getClass().getSimpleName(), oldState.getClass().getSimpleName() + " -> " + newState.getClass().getSimpleName());
	}

	//endregion

	//region Fake

	public void simulateError() {
		this.state.simulateError();
	}

	//endregion
}
