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
	private OnGetMaxDurationListener onGetMaxDurationListener = new NullOnGetMaxDurationListener();
	private OnSeekListener onSeekListener = new NullOnSeekListener();

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

	public void setOnGetMaxDurationListener(OnGetMaxDurationListener onGetMaxDurationListener) {
		this.onGetMaxDurationListener = onGetMaxDurationListener != null
				? onGetMaxDurationListener
				: new NullOnGetMaxDurationListener();
	}

	public void setOnSeekListener(OnSeekListener onSeekListener) {
		this.onSeekListener = onSeekListener != null
				? onSeekListener
				: new NullOnSeekListener();
	}

	//endregion

	//region Play

	public void init() {
		setState(new IdlePlayerState(new MediaPlayer(), this.initializer, this));
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
		this.state = state;
		this.state.set(this.onGetMaxDurationListener, this.onSeekListener);
	}

	private void logState(PlayerState oldState, PlayerState newState) {
		Log.i(getClass().getSimpleName(), oldState.getClass().getSimpleName() + " -> " + newState.getClass().getSimpleName());
	}

	//endregion

	//region Fake

	public void fakeError() {
		this.state.fakeError();
	}

	//endregion
}
