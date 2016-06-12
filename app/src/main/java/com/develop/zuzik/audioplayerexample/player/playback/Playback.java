package com.develop.zuzik.audioplayerexample.player.playback;

import android.content.Context;
import android.content.ContextWrapper;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.develop.zuzik.audioplayerexample.player.player_initializer.PlayerInitializer;
import com.develop.zuzik.audioplayerexample.player.player_states.IdlePlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.NullPlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.Action;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateContainer;

/**
 * User: zuzik
 * Date: 5/29/16
 */
//TODO:investigate logic of changing state and all outer listener, because there was few bugs with null pointer
//TODO:audiomanager can be null
public class Playback implements PlayerStateContainer {

	private MediaPlayer mediaPlayer;
	private PlayerState state = new NullPlayerState();
	public final PlayerInitializer source;
	private PlaybackListener playbackListener = new NullPlaybackListener();
	private boolean repeat;
	private final Context context;
	private final AudioManager audioManager;

	public Playback(Context context, PlayerInitializer source) {
		this.context = new ContextWrapper(context).getApplicationContext();
		this.source = source;
		this.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
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
		this.audioManager.abandonAudioFocus(this.onAudioFocusChangeListener);
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

	private void logState(PlayerState oldState, PlayerState newState) {
		Log.d(getClass().getSimpleName(), oldState.getClass().getSimpleName() + " -> " + newState.getClass().getSimpleName());
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

	//endregion

	//region Fake

	public void simulateError() {
		this.state.simulateError();
	}

	//endregion

	private final AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = focusChange -> {
		if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
			this.state.audioFocusLossTransient();
		} else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
			this.state.audioFocusGain();
			//TODO: in this place we should restore volume to previous normal state
		} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
			this.state.audioFocusLoss();
		} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
			//TODO: in this place we should made volume level lower
		}
	};
}
