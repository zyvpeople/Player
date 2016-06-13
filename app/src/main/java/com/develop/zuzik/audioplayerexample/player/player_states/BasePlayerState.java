package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.develop.zuzik.audioplayerexample.player.exceptions.MediaPlayerStateException;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.player_initializer.PlayerInitializer;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateContainer;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.ResultAction;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.Transformation;

/**
 * User: zuzik
 * Date: 5/29/16
 */
abstract class BasePlayerState implements PlayerState {

	private final boolean allowSetRepeat;
	private final boolean allowSeekToPosition;
	private MediaPlayer player;
	private PlayerInitializer playerInitializer;
	private PlayerStateContainer playerStateContainer;
	private final Transformation<MediaPlayer, PlaybackState> transformation;

	protected BasePlayerState(boolean allowSetRepeat, boolean allowSeekToPosition, Transformation<MediaPlayer, PlaybackState> transformation) {
		this.allowSetRepeat = allowSetRepeat;
		this.allowSeekToPosition = allowSeekToPosition;
		this.transformation = transformation;
	}

	protected final void getPlayer(ResultAction<MediaPlayer> resultAction) {
		try {
			resultAction.execute(this.player);
		} catch (IllegalStateException e) {
			Log.e(getClass().getSimpleName(), e.toString());
			handleError(e);
		}
	}

	protected final PlayerInitializer getPlayerInitializer() {
		return this.playerInitializer;
	}

	protected final void setState(PlayerState state) {
		this.playerStateContainer.setState(state);
	}

	protected final void notifyAboutChanges() {
		this.playerStateContainer.onUpdate();
	}

	protected final void handleError(Throwable throwable) {
		abandonAudioFocus();
		this.player.reset();
		this.playerStateContainer.onError(throwable);
	}

	protected final void abandonAudioFocus() {
		this.playerStateContainer.abandonAudioFocus();
	}

	protected final void stopPlayer() {
		getPlayer(value -> {
			value.stop();
			setState(new IdlePlayerState());
		});
	}

	//region PlayerState

	//FIXME: error is handled by new error appear outside of this code
	@Override
	public final PlaybackState getPlaybackState() {
		return this.transformation.transform(this.player);
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
	public void apply(Context context, MediaPlayer player, PlayerInitializer playerInitializer, PlayerStateContainer playerStateContainer, boolean repeat) {
		this.player = player;
		this.playerInitializer = playerInitializer;
		this.playerStateContainer = playerStateContainer;
		setRepeat(repeat, false);

		this.player.setOnErrorListener((mp, what, extra) -> {
			handleError(new MediaPlayerStateException());
			return true;
		});
		this.player.setOnCompletionListener(mp ->
				getPlayer(value ->
						setState(value.isLooping()
								? new StartedPlayerState()
								: new CompletedPlayerState())));
		this.player.setOnSeekCompleteListener(mp -> notifyAboutChanges());
	}

	@Override
	public void unapply() {
		this.player.setOnErrorListener(null);
		this.player.setOnCompletionListener(null);
		this.player = null;
		this.playerInitializer = null;
		this.playerStateContainer = null;
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
		this.player.release();
	}

	//endregion

}
