package com.develop.zuzik.player.state;

import android.media.MediaPlayer;
import android.util.Log;

import com.develop.zuzik.player.device_sleep.DeviceSleep;
import com.develop.zuzik.player.exception.FailRequestAudioFocusException;
import com.develop.zuzik.player.exception.MediaPlayerStateException;
import com.develop.zuzik.player.exception.PlayerInitializeException;
import com.develop.zuzik.player.interfaces.ParamAction;
import com.develop.zuzik.player.interfaces.State;
import com.develop.zuzik.player.interfaces.VideoViewSetter;
import com.develop.zuzik.player.source.PlayerSource;
import com.develop.zuzik.player.state.interfaces.PlayerState;
import com.develop.zuzik.player.state.interfaces.PlayerStateContext;
import com.develop.zuzik.player.video_view_setter.MediaPlayerVideoViewSetter;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
abstract class BasePlayerState implements PlayerState {

	final PlayerStateContext playerStateContext;
	private final boolean allowSetRepeat;
	private final boolean allowSeekToPosition;
	private MediaPlayerState mediaPlayerState = new MediaPlayerState(State.NONE, 0, Optional.absent());

	protected abstract MediaPlayerState playerToState(MediaPlayer player);

	protected abstract void doOnApply(MediaPlayer player) throws IllegalStateException, PlayerInitializeException, FailRequestAudioFocusException;

	BasePlayerState(
			PlayerStateContext playerStateContext,
			boolean allowSetRepeat,
			boolean allowSeekToPosition) {
		this.playerStateContext = playerStateContext;
		this.allowSetRepeat = allowSetRepeat;
		this.allowSeekToPosition = allowSeekToPosition;
	}

	final MediaPlayer getMediaPlayer() {
		return this.playerStateContext.getMediaPlayer();
	}

	final void allowDeviceSleep() {
		getDeviceSleep().allow();
	}

	final void denyDeviceSleep() {
		getDeviceSleep().deny();
	}

	private DeviceSleep getDeviceSleep() {
		return this.playerStateContext.getDeviceSleep();
	}

	final PlayerSource getPlayerInitializer() {
		return this.playerStateContext.getPlayerSource();
	}

	final void getMediaPlayerSafely(ParamAction<MediaPlayer> action) {
		try {
			action.execute(getMediaPlayer());
		} catch (IllegalStateException e) {
			Log.e(getClass().getSimpleName(), e.toString());
			handleError(e);
		}
	}

	final void applyState(PlayerState state) {
		this.playerStateContext.setPlayerState(state);
	}

	private void saveMediaPlayerState(MediaPlayerState mediaPlayerState) {
		this.mediaPlayerState = mediaPlayerState;
	}

	final void saveMediaPlayerStateAndNotify(MediaPlayerState mediaPlayerState) {
		saveMediaPlayerState(mediaPlayerState);
		this.playerStateContext.onUpdate();
	}

	final void handleError(Throwable throwable) {
		abandonAudioFocus();
		getMediaPlayer().reset();
		allowDeviceSleep();
		this.playerStateContext.onError(throwable);
	}

	final void abandonAudioFocus() {
		this.playerStateContext.abandonAudioFocus();
	}

	final void stopPlayer() {
		getMediaPlayerSafely(value -> {
			value.stop();
			applyState(new IdlePlayerState(this.playerStateContext));
		});
	}

	//region PlayerState

	@Override
	public MediaPlayerState getMediaPlayerState() {
		return this.mediaPlayerState;
	}

	@Override
	public final void videoViewSetter(ParamAction<VideoViewSetter> success) {
		success.execute(new MediaPlayerVideoViewSetter(getMediaPlayer()::setDisplay));
	}

	@Override
	public final void onRepeatChanged() {
		if (this.allowSetRepeat) {
			getMediaPlayerSafely(value -> {
				value.setLooping(this.playerStateContext.isRepeat());
				saveMediaPlayerStateAndNotify(playerToState(value));
			});
		}
	}

	@Override
	public final void apply() throws IllegalStateException, PlayerInitializeException, FailRequestAudioFocusException {
		if (this.allowSetRepeat) {
			getMediaPlayer().setLooping(this.playerStateContext.isRepeat());
		}
		getMediaPlayer().setOnErrorListener((mp, what, extra) -> {
			handleError(new MediaPlayerStateException());
			return true;
		});
		getMediaPlayer().setOnCompletionListener(mp ->
				getMediaPlayerSafely(value ->
						applyState(value.isLooping()
								? new StartedPlayerState(this.playerStateContext)
								: new CompletedPlayerState(this.playerStateContext))));
		getMediaPlayer().setOnSeekCompleteListener(mp ->
				getMediaPlayerSafely(value ->
						saveMediaPlayerStateAndNotify(playerToState(value))));
		doOnApply(getMediaPlayer());
		saveMediaPlayerState(playerToState(getMediaPlayer()));
	}

	@Override
	public void unapply() {
		getMediaPlayer().setOnErrorListener(null);
		getMediaPlayer().setOnCompletionListener(null);
		getMediaPlayer().setOnSeekCompleteListener(null);
	}

	@Override
	public void play() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void stop() {
	}

	@Override
	public void audioFocusLossTransient() {
	}

	@Override
	public void audioFocusGain() {
	}

	@Override
	public void audioFocusLoss() {
	}

	@Override
	public final void simulateError(Throwable throwable) {
		handleError(throwable);
	}

	@Override
	public final void seekTo(int positionInMilliseconds) {
		if (this.allowSeekToPosition) {
			getMediaPlayerSafely(value -> value.seekTo(positionInMilliseconds));
		}
	}

	@Override
	public final void release() {
		allowDeviceSleep();
		getMediaPlayer().release();
	}

	//endregion

}
