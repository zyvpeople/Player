package com.develop.zuzik.audioplayerexample.player.player_states;

import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.exceptions.AudioFocusLostException;
import com.develop.zuzik.audioplayerexample.player.exceptions.FailRequestAudioFocusException;
import com.develop.zuzik.audioplayerexample.player.exceptions.PlayerInitializeException;
import com.develop.zuzik.audioplayerexample.player.playback.MediaPlayerState;
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
	public void apply() throws IllegalStateException, PlayerInitializeException, FailRequestAudioFocusException {
		super.apply();
		this.checkPlayerProgressSubscription = this.checkPlayerProgressObservable
				.subscribe(aLong ->
						getPlayer(value ->
								setMediaPlayerStateAndNotify(playerToState(value))));
		this.playerStateContext
				.requestFocus(
						() -> getMediaPlayer().start(),
						() -> {
							throw new FailRequestAudioFocusException();
						});
		setMediaPlayerState(playerToState(getMediaPlayer()));
	}

	@Override
	public void unapply() {
		this.checkPlayerProgressSubscription.unsubscribe();
		super.unapply();
	}

	@Override
	public void pause() {
		super.pause();
		setState(new ManualPausedPlayerState(this.playerStateContext));
	}

	@Override
	public void stop() {
		super.stop();
		stopPlayer();
	}

	@Override
	public void audioFocusLossTransient() {
		super.audioFocusLossTransient();
		setState(new AudioFocusLostTransientPausedPlayerState(this.playerStateContext));
	}

	@Override
	public void audioFocusLoss() {
		super.audioFocusLoss();
		handleError(new AudioFocusLostException());
	}
}
