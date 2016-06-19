package com.develop.zuzik.audioplayerexample.player.player_states;

import android.media.MediaPlayer;
import android.util.Log;

import com.develop.zuzik.audioplayerexample.player.exceptions.FailRequestAudioFocusException;
import com.develop.zuzik.audioplayerexample.player.exceptions.MediaPlayerStateException;
import com.develop.zuzik.audioplayerexample.player.exceptions.PlayerInitializeException;
import com.develop.zuzik.audioplayerexample.player.playback.MediaPlayerState;
import com.develop.zuzik.audioplayerexample.player.playback.State;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateContext;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.ResultAction;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
abstract class BasePlayerState implements PlayerState {

	protected final PlayerStateContext playerStateContext;
	private final boolean allowSetRepeat;
	private final boolean allowSeekToPosition;
	private MediaPlayerState mediaPlayerState = new MediaPlayerState(State.NONE, 0, Optional.absent());

	protected abstract MediaPlayerState playerToState(MediaPlayer player);

	protected abstract void doOnApply(MediaPlayer player) throws IllegalStateException, PlayerInitializeException, FailRequestAudioFocusException;

	protected BasePlayerState(
			PlayerStateContext playerStateContext,
			boolean allowSetRepeat,
			boolean allowSeekToPosition) {
		this.playerStateContext = playerStateContext;
		this.allowSetRepeat = allowSetRepeat;
		this.allowSeekToPosition = allowSeekToPosition;
	}

	protected final MediaPlayer getMediaPlayer() {
		return this.playerStateContext.getMediaPlayer();
	}

	protected final PlayerSource getPlayerInitializer() {
		return this.playerStateContext.getPlayerInitializer();
	}

	protected final void getMediaPlayerSafely(ResultAction<MediaPlayer> action) {
		try {
			action.execute(getMediaPlayer());
		} catch (IllegalStateException e) {
			Log.e(getClass().getSimpleName(), e.toString());
			handleError(e);
		}
	}

	protected final void applyState(PlayerState state) {
		this.playerStateContext.setPlayerState(state);
	}

	protected final void saveMediaPlayerState(MediaPlayerState mediaPlayerState) {
		this.mediaPlayerState = mediaPlayerState;
	}

	protected final void saveMediaPlayerStateAndNotify(MediaPlayerState mediaPlayerState) {
		saveMediaPlayerState(mediaPlayerState);
		this.playerStateContext.onUpdate();
	}

	protected final void handleError(Throwable throwable) {
		abandonAudioFocus();
		getMediaPlayer().reset();
		this.playerStateContext.onError(throwable);
	}

	protected final void abandonAudioFocus() {
		this.playerStateContext.abandonAudioFocus();
	}

	protected final void stopPlayer() {
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
		getMediaPlayer().release();
	}

	//endregion

}
