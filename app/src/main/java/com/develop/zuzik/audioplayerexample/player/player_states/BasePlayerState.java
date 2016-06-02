package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;
import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.PlayerStateContainer;
import com.develop.zuzik.audioplayerexample.player.player_initializer.PlayerInitializer;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public abstract class BasePlayerState implements PlayerState {

	private final MediaPlayer player;
	private final PlayerInitializer initializer;
	private final PlayerStateContainer stateContainer;

	protected BasePlayerState(MediaPlayer player,
							  PlayerInitializer initializer,
							  PlayerStateContainer stateContainer) {
		this.player = player;
		this.initializer = initializer;
		this.stateContainer = stateContainer;
	}

	protected final MediaPlayer getPlayer() {
		return this.player;
	}

	protected final PlayerInitializer getInitializer() {
		return this.initializer;
	}

	protected final PlayerStateContainer getStateContainer() {
		return this.stateContainer;
	}

	protected final void setState(PlayerState state) {
		getStateContainer().setState(state);
	}

	//region PlayerState

	@Override
	public void set() {
		this.player.setOnErrorListener((mp, what, extra) -> {
			setState(new ErrorPlayerState(getPlayer(), getInitializer(), getStateContainer()));
			return true;
		});
		this.player.setOnCompletionListener(mp ->
				setState(new CompletedPlayerState(getPlayer(), getInitializer(), getStateContainer())));
	}

	@Override
	public void unset() {
		this.player.setOnErrorListener(null);
		this.player.setOnCompletionListener(null);
	}

	@Override
	public void play(Context context) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void stop() {
	}

	//endregion
}
