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
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateBundle;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class Playback implements PlayerStateContainer {

	private MediaPlayer mediaPlayer;
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

		this.state.unset();
		this.state.setPlayer(null);
		this.state.setPlayerSource(null);
		this.state.setPlayerStateContainer(null);
		this.state.setPlaybackListener(null);

		this.state = state;

		this.state.setPlayer(this.mediaPlayer);
		this.state.setPlayerSource(this.source);
		this.state.setPlayerStateContainer(this);
		this.state.setPlaybackListener(this.playbackListener);
		this.state.setRepeat(this.repeat);
		this.state.set(this.context);
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
