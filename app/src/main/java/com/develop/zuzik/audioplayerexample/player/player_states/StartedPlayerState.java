package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;
import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.State;
import com.develop.zuzik.audioplayerexample.player.player_initializer.PlayerInitializer;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateContainer;
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
	public PlaybackState getPlaybackState() {
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
	public void apply(Context context, MediaPlayer player, PlayerInitializer playerInitializer, PlayerStateContainer playerStateContainer, boolean repeat) {
		super.apply(context, player, playerInitializer, playerStateContainer, repeat);
		this.checkPlayerProgressSubscription = this.checkPlayerProgressObservable
				.subscribe(aLong -> notifyAboutChanges());
	}

	@Override
	public void unapply() {
		this.checkPlayerProgressSubscription.unsubscribe();
		super.unapply();
	}

	@Override
	public void pause() {
		super.pause();
		getPlayer().pause();
		setState(new ManualPausedPlayerState());
	}

	@Override
	public void stop() {
		super.stop();
		stopPlayer();
	}

	@Override
	public void audioFocusLossTransient() {
		super.audioFocusLossTransient();
		//TODO: move '.pause()' to 'apply' method of PausedPlayerState
		getPlayer().pause();
		setState(new AudioFocusLostTransientPausedPlayerState());
	}

	@Override
	public void audioFocusLoss() {
		super.audioFocusLoss();
		//TODO: maybe send error or warning because song are stopped not by user
		stopPlayer();
	}
}
