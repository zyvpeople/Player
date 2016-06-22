package com.develop.zuzik.audioplayerexample.player.playback;

import android.content.Context;
import android.content.ContextWrapper;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.develop.zuzik.audioplayerexample.player.exceptions.FailRequestAudioFocusException;
import com.develop.zuzik.audioplayerexample.player.exceptions.FakeMediaPlayerException;
import com.develop.zuzik.audioplayerexample.player.exceptions.PlayerInitializeException;
import com.develop.zuzik.audioplayerexample.player.playback.settings.PlaybackSettings;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.develop.zuzik.audioplayerexample.player.player_states.IdlePlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.NullPlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.Action;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateContext;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class Playback<SourceInfo> implements PlayerStateContext<SourceInfo> {

	private PlaybackState<SourceInfo> playbackState;
	private MediaPlayer mediaPlayer;
	private PlayerState playerState = new NullPlayerState();
	private PlaybackListener playbackListener = new NullPlaybackListener();
	private final Context context;
	private final AudioManager audioManager;
	private final PlaybackSettings settings;

	public Playback(Context context, PlayerSource<SourceInfo> playerSource, PlaybackSettings settings) {
		this.settings = settings;
		this.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		this.context = new ContextWrapper(context).getApplicationContext();
		this.playbackState = new PlaybackState<SourceInfo>(State.NONE, 0, Optional.absent(), this.settings.isRepeat(), playerSource);
	}

	//region Getters/Setters

	public void setPlaybackListener(PlaybackListener playbackListener) {
		this.playbackListener = playbackListener != null
				? playbackListener
				: new NullPlaybackListener();
	}

	public PlaybackState<SourceInfo> getPlaybackState() {
		return this.playbackState;
	}

	public void repeat() {
		this.settings.repeat();
		repeat(true);
	}

	public void doNotRepeat() {
		this.settings.doNotRepeat();
		repeat(false);
	}

	private void repeat(boolean repeat) {
		this.playbackState = this.playbackState.withRepeat(repeat);
		this.playerState.onRepeatChanged();
	}

	//endregion

	//region Play

	public void init() {
		this.mediaPlayer = new MediaPlayer();
		setPlayerState(new IdlePlayerState(this));
	}

	public void release() {
		this.playerState.unapply();
		this.playerState.release();
		abandonAudioFocus();
		this.mediaPlayer = null;
	}

	public void play() {
		this.playerState.play();
	}

	public void pause() {
		this.playerState.pause();
	}

	public void stop() {
		this.playerState.stop();
	}

	public void seekTo(int positionInMilliseconds) {
		this.playerState.seekTo(positionInMilliseconds);
	}

	//endregion

	//region PlayerStateContainer

	@Override
	public void setPlayerState(PlayerState playerState) {
		logState(this.playerState, playerState);
		this.playerState.unapply();
		this.playerState = playerState;
		try {
			this.playerState.apply();
			onUpdate();
		} catch (IllegalStateException | PlayerInitializeException | FailRequestAudioFocusException e) {
			onError(e);
		}
	}

	private void logState(PlayerState oldState, PlayerState newState) {
		Log.d(getClass().getSimpleName(), oldState.getClass().getSimpleName() + " -> " + newState.getClass().getSimpleName());
	}

	@Override
	public void onUpdate() {
		MediaPlayerState mediaPlayerState = this.playerState.getMediaPlayerState();
		this.playbackState = new PlaybackState<SourceInfo>(
				mediaPlayerState.state,
				mediaPlayerState.currentTimeInMilliseconds,
				mediaPlayerState.maxTimeInMilliseconds,
				isRepeat(),
				this.playbackState.playerSource);
		this.playbackListener.onUpdate();
	}

	@Override
	public void onError(Throwable throwable) {
		setPlayerState(new IdlePlayerState(this));
		this.playbackListener.onError(throwable);
	}

	@Override
	public void requestFocus(Action success, Action fail) {
		int result = this.audioManager
				.requestAudioFocus(
						this.onAudioFocusChangeListener,
						AudioManager.STREAM_MUSIC,
						AudioManager.AUDIOFOCUS_GAIN);
		if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
			success.execute();
		} else {
			fail.execute();
		}
	}

	@Override
	public void abandonAudioFocus() {
		this.audioManager.abandonAudioFocus(this.onAudioFocusChangeListener);
	}

	@Override
	public MediaPlayer getMediaPlayer() {
		return this.mediaPlayer;
	}

	@Override
	public PlayerSource<SourceInfo> getPlayerSource() {
		return this.playbackState.playerSource;
	}

	@Override
	public boolean isRepeat() {
		return this.playbackState.repeat;
	}

	@Override
	public Context context() {
		return this.context;
	}

	//endregion

	//region Fake

	public void simulateError() {
		this.playerState.simulateError(new FakeMediaPlayerException());
	}

	//endregion

	private final AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = focusChange -> {
		if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
			this.playerState.audioFocusLossTransient();
		} else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
			this.playerState.audioFocusGain();
			//TODO: in this place we should restore volume to previous normal state
		} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
			this.playerState.audioFocusLoss();
		} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
			//TODO: in this place we should made volume level lower
		}
	};
}
