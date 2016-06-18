package com.develop.zuzik.audioplayerexample.player.player_states;

import android.media.MediaPlayer;
import android.util.Log;

import com.develop.zuzik.audioplayerexample.player.exceptions.MediaPlayerStateException;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.player_initializer.PlayerInitializer;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateContext;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.ResultAction;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.Transformation;

/**
 * User: zuzik
 * Date: 5/29/16
 */
abstract class BasePlayerState implements PlayerState {

	protected final PlayerStateContext playerStateContext;
	private final boolean allowSetRepeat;
	private final boolean allowSeekToPosition;
	private final Transformation<MediaPlayer, PlaybackState> transformation;

	protected BasePlayerState(PlayerStateContext playerStateContext, boolean allowSetRepeat, boolean allowSeekToPosition, Transformation<MediaPlayer, PlaybackState> transformation) {
		this.playerStateContext = playerStateContext;
		this.allowSetRepeat = allowSetRepeat;
		this.allowSeekToPosition = allowSeekToPosition;
		this.transformation = transformation;
	}

	protected final MediaPlayer getMediaPlayer() {
		return this.playerStateContext.getMediaPlayer();
	}

	protected final PlayerInitializer getPlayerInitializer() {
		return this.playerStateContext.getPlayerInitializer();
	}

	protected final void getPlayer(ResultAction<MediaPlayer> resultAction) {
		try {
			resultAction.execute(getMediaPlayer());
		} catch (IllegalStateException e) {
			Log.e(getClass().getSimpleName(), e.toString());
			handleError(e);
		}
	}

	protected final void setState(PlayerState state) {
		this.playerStateContext.setState(state);
	}

	protected final void notifyAboutChanges() {
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
		getPlayer(value -> {
			value.stop();
			setState(new IdlePlayerState(this.playerStateContext));
		});
	}

	//region PlayerState

	@Override
	public final PlaybackState getPlaybackState() {
		return this.transformation.transform(getMediaPlayer());
	}

	@Override
	public final void setRepeat(boolean repeat) {
		setRepeat(repeat, true);
	}

	private void setRepeat(boolean repeat, boolean notifyAboutChanges) {
		if (this.allowSetRepeat) {
			getPlayer(value -> {
				value.setLooping(repeat);
				if (notifyAboutChanges) {
					notifyAboutChanges();
				}
			});
		}
	}

	@Override
	public void apply() {
		setRepeat(this.playerStateContext.isRepeat(), false);

		getMediaPlayer().setOnErrorListener((mp, what, extra) -> {
			handleError(new MediaPlayerStateException());
			return true;
		});
		getMediaPlayer().setOnCompletionListener(mp ->
				getPlayer(value ->
						setState(value.isLooping()
								? new StartedPlayerState(this.playerStateContext)
								: new CompletedPlayerState(this.playerStateContext))));
		getMediaPlayer().setOnSeekCompleteListener(mp -> notifyAboutChanges());
	}

	@Override
	public void unapply() {
		getMediaPlayer().setOnErrorListener(null);
		getMediaPlayer().setOnCompletionListener(null);
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
			getPlayer(value -> value.seekTo(positionInMilliseconds));
		}
	}

	@Override
	public final void release() {
		getMediaPlayer().release();
	}

	//endregion

}
