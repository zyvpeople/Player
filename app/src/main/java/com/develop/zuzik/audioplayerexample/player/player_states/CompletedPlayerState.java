package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;
import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.PlayerStateBundle;
import com.develop.zuzik.audioplayerexample.player.interfaces.PlayerStateContainer;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class CompletedPlayerState extends BasePlayerState {

	public CompletedPlayerState(MediaPlayer player, PlayerSource source, PlayerStateContainer stateContainer) {
		super(player, source, stateContainer);
	}

	@Override
	public PlayerStateBundle getPlayerStateBundle() {
		int maxDuration = getPlayer().getDuration();
		return new PlayerStateBundle(
				PlaybackState.COMPLETED,
				maxDuration,
				Optional.of(maxDuration),
				getPlayer().isLooping());
	}

	@Override
	public void setRepeat(boolean repeat) {
		super.setRepeat(repeat);
		getPlayer().setLooping(repeat);
	}

	@Override
	public void set(Context context) {
		super.set(context);
		onPlaybackStateChanged();
	}

	@Override
	public void play() {
		super.play();
		getPlayer().start();
		setState(new StartedPlayerState(getPlayer(), getSource(), getStateContainer()));
	}

	@Override
	public void stop() {
		super.stop();
		getPlayer().stop();
		getPlayer().reset();
		setState(new IdlePlayerState(getPlayer(), getSource(), getStateContainer()));
	}

	@Override
	public void seekTo(int positionInMilliseconds) {
		super.seekTo(positionInMilliseconds);
		seekToPosition(positionInMilliseconds);
	}

	@Override
	protected void onSeekCompleted() {
		super.onSeekCompleted();
		onPlaybackStateChanged();
	}
}
