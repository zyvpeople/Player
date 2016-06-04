package com.develop.zuzik.audioplayerexample.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.RawRes;
import android.util.Log;

import com.develop.zuzik.audioplayerexample.player.player_initializer.PlayerInitializer;
import com.develop.zuzik.audioplayerexample.player.player_initializer.RawResourcePlayerInitializer;
import com.develop.zuzik.audioplayerexample.player.player_initializer.UriPlayerInitializer;
import com.develop.zuzik.audioplayerexample.player.player_states.IdlePlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.NullPlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.PlayerState;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class Playback implements PlayerStateContainer {

	private PlayerState state = new NullPlayerState();
	private final PlayerInitializer initializer;
	private PlaybackListener playbackListener = new NullPlaybackListener();
	private boolean repeat;

	public Playback(Uri uri) {
		this(new UriPlayerInitializer(uri));
	}

	public Playback(@RawRes int rawResId) {
		this(new RawResourcePlayerInitializer(rawResId));
	}

	private Playback(PlayerInitializer initializer) {
		this.initializer = initializer;
	}

	//region Getters/Setters

	public void setPlaybackListener(PlaybackListener playbackListener) {
		this.playbackListener = playbackListener != null
				? playbackListener
				: new NullPlaybackListener();
		this.state.setPlaybackListener(this.playbackListener);
	}

	public PlaybackBundle getPlaybackBundle() {
		return this.state.getPlaybackBundle();
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
		this.state.setRepeat(this.repeat);
	}

	//endregion

	//region Play

	public void init() {
		setState(new IdlePlayerState(new MediaPlayer(), this.initializer, this));
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
		this.state.fakeError();
	}

	//endregion
}
