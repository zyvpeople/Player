package com.develop.zuzik.audioplayerexample.player;

import android.content.Context;
import android.content.ContextWrapper;
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
	private final Context context;

	public Playback(Context context, PlayerSource source) {
		this.context = new ContextWrapper(context).getBaseContext();
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

	public void repeat() {
		this.repeat = true;
		this.state.setRepeat(true);
	}

	public void doNotRepeat() {
		this.repeat = false;
		this.state.setRepeat(false);
	}

	//endregion

	//region Play

	public void init() {
		setState(new IdlePlayerState(new MediaPlayer(), this.source, this));
	}

	public void release() {
		this.state.release();
	}

	public void play() {
		this.state.play();
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
		this.state.set(this.context);
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
