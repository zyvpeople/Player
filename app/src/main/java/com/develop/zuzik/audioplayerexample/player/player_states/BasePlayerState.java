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

	protected final void handleError() {
		getPlayer().reset();
		this.playerStateContainer.onError();
	}

	//region PlayerState

	@Override
	public final void setPlayer(MediaPlayer player) {
		this.player = player;
	}

	public final void setPlayerInitializer(PlayerInitializer playerInitializer) {
		this.playerInitializer = playerInitializer;
	}

	@Override
	public final void setPlayerStateContainer(PlayerStateContainer playerStateContainer) {
		this.playerStateContainer = playerStateContainer;
	}

	@Override
	public final void setRepeat(boolean repeat) {
		if (this.allowSetRepeat) {
			getPlayer().setLooping(repeat);
			notifyAboutChanges();
		}
	}

	@Override
	public void set(Context context) {
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
	public void unset() {
		this.player.setOnErrorListener(null);
		this.player.setOnCompletionListener(null);
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
		setState(new EndPlayerState());
	}

	//endregion

}
