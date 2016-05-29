package com.develop.zuzik.audioplayerexample.player;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.util.Log;

import com.develop.zuzik.audioplayerexample.player.exceptions.CreatePlayerException;
import com.develop.zuzik.audioplayerexample.player.exceptions.PlayerAlreadyInitializedException;
import com.develop.zuzik.audioplayerexample.player.exceptions.UnknownPlayerSourceException;
import com.develop.zuzik.audioplayerexample.player.player_sources.PlayerSource;
import com.develop.zuzik.audioplayerexample.player.player_sources.RawResourcePlayerSource;
import com.develop.zuzik.audioplayerexample.player.player_states.ErrorPlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.NullPlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.PlaybackCompletedPlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.PlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.PreparedPlayerState;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class Playback implements PlayerStateContainer {

	private PlayerState state = new NullPlayerState();
	private MediaPlayer player;

	//region Initialization

	public void initWithPlayerSource(Context context, PlayerSource playerSource)
			throws UnknownPlayerSourceException, PlayerAlreadyInitializedException, CreatePlayerException {
		checkIfPlayerInitialized();
		if (playerSource instanceof RawResourcePlayerSource) {
			initWithRawResourcePlayerSource(context, (RawResourcePlayerSource) playerSource);
		} else {
			throw new UnknownPlayerSourceException();
		}
	}

	private void initWithRawResourcePlayerSource(Context context, RawResourcePlayerSource playerSource) throws CreatePlayerException {
		try {
			this.player = MediaPlayer.create(context, playerSource.rawResId);
		} catch (Resources.NotFoundException e) {
			throw new CreatePlayerException();
		}
		if (this.player != null) {
			setListeners();
			setState(new PreparedPlayerState(this.player, this));
		} else {
			throw new CreatePlayerException();
		}
	}

	private void checkIfPlayerInitialized() throws PlayerAlreadyInitializedException {
		if (this.player != null) {
			throw new PlayerAlreadyInitializedException();
		}
	}

	private void setListeners() {
		this.player.setOnPreparedListener(this.onPreparedListener);
		this.player.setOnCompletionListener(this.onCompletionListener);
		this.player.setOnErrorListener(this.onErrorListener);
	}

	//endregion

	//region Play

	public void start() {
		this.state.start();
	}

	public void pause() {
		this.state.pause();
	}

	public void stop() {
		this.state.stop();
	}

	//endregion

	//region PlayerStateContainer

	@Override
	public void setState(PlayerState state) {
		logState(this.state, state);
		this.state = state;
	}

	private void logState(PlayerState oldState, PlayerState newState) {
		Log.i(getClass().getSimpleName(), oldState.getClass().getSimpleName() + " -> " + newState.getClass().getSimpleName());
	}

	//endregion

	//region Listeners

	private final MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
		@Override
		public void onPrepared(MediaPlayer mp) {
			setState(new PreparedPlayerState(player, Playback.this));
		}
	};

	private final MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
		@Override
		public void onCompletion(MediaPlayer mp) {
			setState(new PlaybackCompletedPlayerState(player, Playback.this));
		}
	};

	private final MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			setState(new ErrorPlayerState(player, Playback.this));
			return true;
		}
	};

	//endregion
}
