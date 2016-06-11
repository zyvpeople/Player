package com.develop.zuzik.audioplayerexample.player.playback;

import android.content.Context;
import android.content.ContextWrapper;
import android.media.MediaPlayer;
import android.util.Log;

import com.develop.zuzik.audioplayerexample.player.player_initializer.PlayerInitializer;
import com.develop.zuzik.audioplayerexample.player.player_states.IdlePlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.NullPlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateContainer;

/**
 * User: zuzik
 * Date: 5/29/16
 */
//TODO:investigate logic of changing state and all outer listener, because there was few bugs with null pointer
public class Playback implements PlayerStateContainer {

	private MediaPlayer mediaPlayer;
	private PlayerState state = new NullPlayerState();
	public final PlayerInitializer source;
	private PlaybackListener playbackListener = new NullPlaybackListener();
	private boolean repeat;
	private final Context context;

	public Playback(Context context, PlayerInitializer source) {
		this.context = new ContextWrapper(context).getBaseContext();
		this.source = source;
	}

	//region Getters/Setters

	public void setPlaybackListener(PlaybackListener playbackListener) {
		this.playbackListener = playbackListener != null
				? playbackListener
				: new NullPlaybackListener();
	}

	public PlaybackState getState() {
		return this.state.getPlaybackState();
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
		this.mediaPlayer = new MediaPlayer();
		setState(new IdlePlayerState());
	}

	public void release() {
		this.state.release();
		this.mediaPlayer = null;
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
		this.state.unapply();
		this.state = state;
		this.state.apply(this.context, this.mediaPlayer, this.source, this, this.repeat);
	}

	@Override
	public void onUpdate() {
		this.playbackListener.onUpdate();
	}

	@Override
	public void onError() {
		setState(new IdlePlayerState());
		this.playbackListener.onError();
	}

	private void logState(PlayerState oldState, PlayerState newState) {
		Log.d(getClass().getSimpleName(), oldState.getClass().getSimpleName() + " -> " + newState.getClass().getSimpleName());
	}
	//endregion

	//region Fake

	public void simulateError() {
		this.state.simulateError();
	}

	//endregion
}
