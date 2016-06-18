package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;
import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.exceptions.AudioFocusLostException;
import com.develop.zuzik.audioplayerexample.player.exceptions.FailRequestAudioFocusException;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.State;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateContext;
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
		super(true, true, player -> {
			int maxDuration = player.getDuration();
			return new PlaybackState(
					State.PLAYING,
					player.getCurrentPosition(),
					maxDuration != -1
							? Optional.of(maxDuration)
							: Optional.absent(),
					player.isLooping());
		});
	}

	@Override
	public void apply(Context context, PlayerStateContext playerStateContext) {
		super.apply(context, playerStateContext);
		this.checkPlayerProgressSubscription = this.checkPlayerProgressObservable
				.subscribe(aLong -> notifyAboutChanges());
		playerStateContext.requestFocus(() -> getPlayer(MediaPlayer::start), () -> handleError(new FailRequestAudioFocusException()));
	}

	@Override
	public void unapply() {
		this.checkPlayerProgressSubscription.unsubscribe();
		super.unapply();
	}

	@Override
	public void pause() {
		super.pause();
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
		setState(new AudioFocusLostTransientPausedPlayerState());
	}

	@Override
	public void audioFocusLoss() {
		super.audioFocusLoss();
		handleError(new AudioFocusLostException());
	}
}
