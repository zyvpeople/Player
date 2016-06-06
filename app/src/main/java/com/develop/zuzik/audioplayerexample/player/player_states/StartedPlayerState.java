package com.develop.zuzik.audioplayerexample.player.player_states;

import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.PlayerStateBundle;
import com.develop.zuzik.audioplayerexample.player.interfaces.PlayerStateContainer;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

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
			.interval(1, TimeUnit.SECONDS)
			.observeOn(AndroidSchedulers.mainThread());
	private Subscription playerProgressSubscription;

	public StartedPlayerState(MediaPlayer player, PlayerSource source, PlayerStateContainer stateContainer) {
		super(player, source, stateContainer);
	}

	@Override
	public PlayerStateBundle getPlayerStateBundle() {
		int maxDuration = getPlayer().getDuration();
		return new PlayerStateBundle(
				PlaybackState.PLAYING,
				getPlayer().getCurrentPosition(),
				maxDuration != -1
						? Optional.of(maxDuration)
						: Optional.absent(),
				getPlayer().isLooping());
	}

	@Override
	public void setRepeat(boolean repeat) {
		super.setRepeat(repeat);
		getPlayer().setLooping(repeat);
	}

	@Override
	public void set() {
		super.set();
		onPlaybackStateChanged();
		this.playerProgressSubscription = this.playerProgressObservable.subscribe(aLong ->
				onPlaybackStateChanged());
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
		setState(new PausedPlayerState(getPlayer(), getSource(), getStateContainer()));
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
