package com.develop.zuzik.audioplayerexample.player.player_states;

import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.PlaybackBundle;
import com.develop.zuzik.audioplayerexample.player.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.PlayerStateContainer;
import com.develop.zuzik.audioplayerexample.player.player_initializer.PlayerInitializer;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class StartedPlayerState extends BasePlayerState {

	private final Observable<Long> playerProgressObservable = Observable
			.interval(10, TimeUnit.SECONDS)
			.observeOn(AndroidSchedulers.mainThread());
	private Subscription playerProgressSubscription;

	public StartedPlayerState(MediaPlayer player, PlayerInitializer initializer, PlayerStateContainer stateContainer) {
		super(player, initializer, stateContainer);
	}

	@Override
	public PlaybackBundle getPlaybackBundle() {
		return createBundle(PlaybackState.PLAYING);
	}

	@Override
	public void set() {
		super.set();
		onPlaybackStateChanged(PlaybackState.PLAYING);
		this.playerProgressSubscription = this.playerProgressObservable.subscribe(aLong ->
				onPlaybackStateChanged(PlaybackState.PLAYING));
	}

	@Override
	public void unset() {
		this.playerProgressSubscription.unsubscribe();
		super.unset();
	}

	@Override
	public void pause() {
		super.pause();
		getPlayer().pause();
		setState(new PausedPlayerState(getPlayer(), getInitializer(), getStateContainer()));
	}

	@Override
	public void stop() {
		super.stop();
		getPlayer().stop();
		getPlayer().reset();
		setState(new IdlePlayerState(getPlayer(), getInitializer(), getStateContainer()));
	}

	@Override
	public void seekTo(int positionInMilliseconds) {
		super.seekTo(positionInMilliseconds);
		seekToPosition(positionInMilliseconds);
	}

	@Override
	protected void onSeekCompleted() {
		super.onSeekCompleted();
		onPlaybackStateChanged(PlaybackState.PLAYING);
	}
}
