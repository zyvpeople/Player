package com.develop.zuzik.audioplayerexample.player;

import android.content.Context;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.develop.zuzik.audioplayerexample.player.exceptions.CreatePlayerException;
import com.develop.zuzik.audioplayerexample.player.exceptions.PlayerAlreadyInitializedException;
import com.develop.zuzik.audioplayerexample.player.exceptions.UnknownPlayerSourceException;
import com.develop.zuzik.audioplayerexample.player.player_sources.PlayerSource;
import com.develop.zuzik.audioplayerexample.player.player_sources.RawResourcePlayerSource;
import com.develop.zuzik.audioplayerexample.player.player_sources.UriPlayerSource;
import com.develop.zuzik.audioplayerexample.player.player_states.ErrorPlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.IdlePlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.InitializedPlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.NullPlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.PlaybackCompletedPlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.BasePlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.PreparedPlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.PreparingPlayerState;

import java.io.IOException;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class Playback implements PlayerStateContainer {

	private BasePlayerState state = new NullPlayerState();
	private MediaPlayer player;
	private PlayerSource playerSource;

	//region Initialization

	public void initWithPlayerSource(Context context, PlayerSource playerSource)
			throws UnknownPlayerSourceException, PlayerAlreadyInitializedException, CreatePlayerException {
		checkIfPlayerInitialized();
		this.playerSource = playerSource;
		if (playerSource instanceof RawResourcePlayerSource) {
			initWithRawResourcePlayerSource(context, (RawResourcePlayerSource) playerSource);
		} else if (playerSource instanceof UriPlayerSource) {
			initWithUriPlayerSource(context, (UriPlayerSource) playerSource);
		} else {
			throw new UnknownPlayerSourceException();
		}
	}

	private void checkIfPlayerInitialized() throws PlayerAlreadyInitializedException {
		if (this.player != null) {
			throw new PlayerAlreadyInitializedException();
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

	private void initWithUriPlayerSource(Context context, UriPlayerSource playerSource) throws CreatePlayerException {
		this.player = new MediaPlayer();
		setState(new IdlePlayerState(this.player, this));
		this.player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			this.player.setDataSource(context, playerSource.uri);
			setState(new InitializedPlayerState(this.player, this));
			setListeners();
			setState(new PreparingPlayerState(this.player, this));
			this.player.prepareAsync();
		} catch (IOException | IllegalArgumentException | SecurityException | IllegalStateException e) {
			throw new CreatePlayerException();
		}
	}

	private void setListeners() {
		this.player.setOnPreparedListener(this.onPreparedListener);
		this.player.setOnCompletionListener(this.onCompletionListener);
		this.player.setOnErrorListener(this.onErrorListener);
	}

	//endregion

	//region Play

	public void start(Context context) {
		this.state.start(context);
	}

	public void pause(Context context) {
		this.state.pause(context);
	}

	public void stop(Context context) {
		this.state.stop(context);
	}

	//endregion

	//region PlayerStateContainer

	@Override
	public void setState(BasePlayerState state) {
		logState(this.state, state);
		this.state.onUnset();
		this.state = state;
		this.state.onSet();
	}

	private void logState(BasePlayerState oldState, BasePlayerState newState) {
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
