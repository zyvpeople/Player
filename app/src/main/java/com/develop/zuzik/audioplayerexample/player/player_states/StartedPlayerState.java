package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.player.playback.State;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
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

	private final Observable<Long> checkPlayerProgressObservable = Observable
			.interval(1, TimeUnit.SECONDS)
			.observeOn(AndroidSchedulers.mainThread());
	private Subscription checkPlayerProgressSubscription;

	public StartedPlayerState() {
		super(true, true);
	}

	@Override
	public PlaybackState getPlayerStateBundle() {
		int maxDuration = getPlayer().getDuration();
		return new PlaybackState(
				State.PLAYING,
				getPlayer().getCurrentPosition(),
				maxDuration != -1
						? Optional.of(maxDuration)
						: Optional.absent(),
				getPlayer().isLooping());
	}

	@Override
	public void set(Context context) {
		super.set(context);
		this.checkPlayerProgressSubscription = this.checkPlayerProgressObservable
				.subscribe(aLong -> notifyAboutChanges());
	}

	@Override
	public void unset() {
		this.checkPlayerProgressSubscription.unsubscribe();
		super.unset();
	}

	@Override
	public void pause() {
		super.pause();
		getPlayer().pause();
		setState(new PausedPlayerState());
	}

	@Override
	public void stop() {
		super.stop();
		getPlayer().stop();
		getPlayer().reset();
		setState(new IdlePlayerState());
	}
}
