package com.develop.zuzik.player.local;

import android.content.Context;
import android.content.ContextWrapper;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.util.Log;

import com.develop.zuzik.player.device_sleep.DeviceSleep;
import com.develop.zuzik.player.exception.FailRequestAudioFocusException;
import com.develop.zuzik.player.exception.FakeMediaPlayerException;
import com.develop.zuzik.player.exception.PlayerInitializeException;
import com.develop.zuzik.player.interfaces.Action;
import com.develop.zuzik.player.interfaces.Playback;
import com.develop.zuzik.player.interfaces.PlaybackListener;
import com.develop.zuzik.player.interfaces.PlaybackState;
import com.develop.zuzik.player.interfaces.State;
import com.develop.zuzik.player.null_object.NullDeviceSleep;
import com.develop.zuzik.player.null_object.NullPlaybackListener;
import com.develop.zuzik.player.source.PlayerSource;
import com.develop.zuzik.player.state.IdlePlayerState;
import com.develop.zuzik.player.state.MediaPlayerState;
import com.develop.zuzik.player.state.NullPlayerState;
import com.develop.zuzik.player.state.interfaces.PlayerState;
import com.develop.zuzik.player.state.interfaces.PlayerStateContext;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class LocalPlayback<SourceInfo> implements Playback<SourceInfo>, PlayerStateContext<SourceInfo> {

	private final Context context;
	private final AudioManager audioManager;
	private PlaybackState<SourceInfo> playbackState;
	private PlaybackListener<SourceInfo> playbackListener = NullPlaybackListener.getInstance();
	private PlayerState playerState = NullPlayerState.INSTANCE;
	private MediaPlayer mediaPlayer;
	private DeviceSleep deviceSleep = NullDeviceSleep.INSTANCE;

	public LocalPlayback(Context context, boolean repeat, PlayerSource<SourceInfo> playerSource) {
		this.context = new ContextWrapper(context).getApplicationContext();
		this.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		this.playbackState = new PlaybackState<>(State.NONE, 0, Optional.absent(), repeat, playerSource);
	}

	//region Playback

	@Override
	public PlaybackState<SourceInfo> getPlaybackState() {
		return this.playbackState;
	}

	@Override
	public void setPlaybackListener(PlaybackListener<SourceInfo> playbackListener) {
		this.playbackListener = playbackListener != null ? playbackListener : NullPlaybackListener.getInstance();
	}

	@Override
	public void init() {
		this.mediaPlayer = new MediaPlayer();
		this.mediaPlayer.setWakeMode(this.context, PowerManager.PARTIAL_WAKE_LOCK);
		this.deviceSleep = getPlayerSource().createDeviceSleep(this.context);
		setPlayerState(new IdlePlayerState(this));
	}

	@Override
	public void release() {
		this.playerState.unapply();
		this.playerState.release();
		abandonAudioFocus();
		this.mediaPlayer = null;
	}

	@Override
	public void play() {
		this.playerState.play();
	}

	@Override
	public void pause() {
		this.playerState.pause();
	}

	@Override
	public void stop() {
		this.playerState.stop();
	}

	@Override
	public void seekTo(int positionInMilliseconds) {
		this.playerState.seekTo(positionInMilliseconds);
	}

	@Override
	public void repeat() {
		repeat(true);
	}

	@Override
	public void doNotRepeat() {
		repeat(false);
	}

	@Override
	public void simulateError() {
		this.playerState.simulateError(new FakeMediaPlayerException());
	}

	private void repeat(boolean repeat) {
		this.playbackState = this.playbackState.withRepeat(repeat);
		this.playerState.onRepeatChanged();
	}

	//endregion

	//region PlayerStateContainer

	@Override
	public MediaPlayer getMediaPlayer() {
		return this.mediaPlayer;
	}

	@Override
	public PlayerSource<SourceInfo> getPlayerSource() {
		return this.playbackState.playerSource;
	}

	@Override
	public DeviceSleep getDeviceSleep() {
		return this.deviceSleep;
	}

	@Override
	public boolean isRepeat() {
		return this.playbackState.repeat;
	}

	@Override
	public Context context() {
		return this.context;
	}

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

	@Override
	public void onUpdate() {
		MediaPlayerState mediaPlayerState = this.playerState.getMediaPlayerState();
		this.playbackState = new PlaybackState<>(
				mediaPlayerState.state,
				mediaPlayerState.currentTimeInMilliseconds,
				mediaPlayerState.maxTimeInMilliseconds,
				isRepeat(),
				this.playbackState.playerSource);
		this.playbackListener.onUpdate(this.playbackState);
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

	private void logState(PlayerState oldState, PlayerState newState) {
		Log.d(getClass().getSimpleName(), oldState.getClass().getSimpleName() + " -> " + newState.getClass().getSimpleName());
	}

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

	//endregion
}
