package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;
import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.NullPlaybackListener;
import com.develop.zuzik.audioplayerexample.player.PlaybackBundle;
import com.develop.zuzik.audioplayerexample.player.PlaybackListener;
import com.develop.zuzik.audioplayerexample.player.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.PlayerStateContainer;
import com.develop.zuzik.audioplayerexample.player.player_initializer.PlayerSource;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public abstract class BasePlayerState implements PlayerState {

	private final MediaPlayer player;
	private final PlayerSource source;
	private final PlayerStateContainer stateContainer;
	private PlaybackListener playbackListener = new NullPlaybackListener();

	protected BasePlayerState(MediaPlayer player,
							  PlayerSource source,
							  PlayerStateContainer stateContainer) {
		this.player = player;
		this.source = source;
		this.stateContainer = stateContainer;
	}

	protected final MediaPlayer getPlayer() {
		return this.player;
	}

	protected final PlayerSource getSource() {
		return this.source;
	}

	protected final PlayerStateContainer getStateContainer() {
		return this.stateContainer;
	}

	protected final void setState(PlayerState state) {
		getStateContainer().setState(state);
	}

	protected final void onPlaybackStateChanged(PlaybackState state) {
		this.playbackListener.onChange(createBundle(state));
	}

	protected final void onPlaybackStateChanged(PlaybackBundle bundle) {
		this.playbackListener.onChange(bundle);
	}

	protected final PlaybackBundle createBundle(PlaybackState state) {
		int currentPosition = getPlayer().getCurrentPosition();
		int maxDuration = getPlayer().getDuration();
		Integer maxDurationOrNull = maxDuration != -1 ? maxDuration : null;
		return new PlaybackBundle(state, currentPosition, maxDurationOrNull, getPlayer().isLooping());
	}

	protected void onSeekCompleted() {
	}

	//region PlayerState

	@Override
	public void setPlaybackListener(PlaybackListener playbackListener) {
		this.playbackListener = playbackListener != null
				? playbackListener
				: new NullPlaybackListener();
	}

	@Override
	public void setRepeat(boolean repeat) {
	}

	@Override
	public void set() {
		this.player.setOnErrorListener((mp, what, extra) -> {
			handleError();
			return true;
		});
		this.player.setOnCompletionListener(mp -> {
			if (getPlayer().isLooping()) {
				setState(new StartedPlayerState(getPlayer(), getSource(), getStateContainer()));
			} else {
				setState(new CompletedPlayerState(getPlayer(), getSource(), getStateContainer()));
			}
		});
		this.player.setOnSeekCompleteListener(mp -> onSeekCompleted());
	}

	protected final void handleError() {
		getPlayer().reset();
		onPlaybackStateChanged(new PlaybackBundle(PlaybackState.ERROR, 0, null, getPlayer().isLooping()));
		setState(new IdlePlayerState(getPlayer(), getSource(), getStateContainer()));
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

	@Override
	public void simulateError() {
		handleError();
	}

	@Override
	public void seekTo(int positionInMilliseconds) {
	}

	@Override
	public void release() {
		getPlayer().release();
		setState(new EndPlayerState());
	}

	protected final void seekToPosition(int positionInMilliseconds) {
		getPlayer().seekTo(positionInMilliseconds);
	}

	//endregion

}
