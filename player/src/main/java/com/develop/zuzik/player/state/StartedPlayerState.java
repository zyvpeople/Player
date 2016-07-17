package com.develop.zuzik.player.state;

import android.media.MediaPlayer;

import com.develop.zuzik.player.exception.AudioFocusLostException;
import com.develop.zuzik.player.exception.FailRequestAudioFocusException;
import com.develop.zuzik.player.exception.PlayerInitializeException;
import com.develop.zuzik.player.interfaces.State;
import com.develop.zuzik.player.state.interfaces.PlayerStateContext;
import com.fernandocejas.arrow.optional.Optional;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * User: zuzik
 * Date: 5/29/16
 */
class StartedPlayerState extends BasePlayerState {

	private final Observable<Long> checkPlayerProgressObservable = Observable
			.interval(1, TimeUnit.SECONDS)
			.observeOn(AndroidSchedulers.mainThread());
	private Subscription checkPlayerProgressSubscription;

	public StartedPlayerState(PlayerStateContext playerStateContext) {
		super(playerStateContext, true, true);
	}

	@Override
	protected MediaPlayerState playerToState(MediaPlayer player) {
		int maxDuration = player.getDuration();
		return new MediaPlayerState(
				State.PLAYING,
				player.getCurrentPosition(),
				maxDuration != -1
						? Optional.of(maxDuration)
						: Optional.absent());
	}

	@Override
	protected void doOnApply(MediaPlayer player) throws IllegalStateException, PlayerInitializeException, FailRequestAudioFocusException {
		this.checkPlayerProgressSubscription = this.checkPlayerProgressObservable
				.onErrorResumeNext(throwable -> Observable.just(0L))
				.subscribe(aLong ->
						getMediaPlayerSafely(value ->
								saveMediaPlayerStateAndNotify(playerToState(value))));
		this.playerStateContext
				.requestFocus(
						player::start,
						() -> {
							throw new FailRequestAudioFocusException();
						});
	}

	@Override
	public void unapply() {
		this.checkPlayerProgressSubscription.unsubscribe();
		super.unapply();
	}

	@Override
	public void pause() {
		super.pause();
		applyState(new ManualPausedPlayerState(this.playerStateContext));
	}

	@Override
	public void stop() {
		super.stop();
		stopPlayer();
	}

	@Override
	public void audioFocusLossTransient() {
		super.audioFocusLossTransient();
		applyState(new AudioFocusLostTransientPausedPlayerState(this.playerStateContext));
	}

	@Override
	public void audioFocusLoss() {
		super.audioFocusLoss();
		handleError(new AudioFocusLostException());
	}
}
