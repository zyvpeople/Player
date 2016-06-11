package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;
import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.player_initializer.PlayerInitializer;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateContainer;

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

	protected BasePlayerState(boolean allowSetRepeat, boolean allowSeekToPosition) {
		this.allowSetRepeat = allowSetRepeat;
		this.allowSeekToPosition = allowSeekToPosition;
	}

	protected final MediaPlayer getPlayer() {
		return this.player;
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

	//TODO: send concrete throwable
	protected final void handleError() {
		abandonAudioFocus();
		getPlayer().reset();
		this.playerStateContainer.onError();
	}

	protected final void abandonAudioFocus() {
		this.playerStateContainer.abandonAudioFocus();
	}

	protected final void startPlayer() {
		this.playerStateContainer.requestFocus(() -> {
			getPlayer().start();
			setState(new StartedPlayerState());
		}, () -> handleError());
	}

	//region PlayerState

	@Override
	public final void setRepeat(boolean repeat) {
		if (this.allowSetRepeat) {
			getPlayer().setLooping(repeat);
			notifyAboutChanges();
		}
	}

	@Override
	public void apply(Context context, MediaPlayer player, PlayerInitializer playerInitializer, PlayerStateContainer playerStateContainer, boolean repeat) {
		this.player = player;
		this.playerInitializer = playerInitializer;
		this.playerStateContainer = playerStateContainer;
		setRepeat(repeat);

		this.player.setOnErrorListener((mp, what, extra) -> {
			handleError();
			return true;
		});
		this.player.setOnCompletionListener(mp ->
				setState(getPlayer().isLooping()
						? new StartedPlayerState()
						: new CompletedPlayerState()));
		this.player.setOnSeekCompleteListener(mp -> notifyAboutChanges());
		notifyAboutChanges();
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
	public final void simulateError() {
		handleError();
	}

	@Override
	public final void seekTo(int positionInMilliseconds) {
		if (this.allowSeekToPosition) {
			getPlayer().seekTo(positionInMilliseconds);
		}
	}

	@Override
	public final void release() {
		getPlayer().release();
	}

	//endregion

}
