package com.develop.zuzik.player.state;

import android.media.MediaPlayer;

import com.develop.zuzik.player.exception.AudioFocusLostException;
import com.develop.zuzik.player.exception.FailRequestAudioFocusException;
import com.develop.zuzik.player.exception.PlayerInitializeException;
import com.develop.zuzik.player.interfaces.State;
import com.develop.zuzik.player.state.interfaces.PlayerStateContext;
import com.develop.zuzik.player.timer.PeriodicAction;
import com.fernandocejas.arrow.optional.Optional;


/**
 * User: zuzik
 * Date: 5/29/16
 */
class StartedPlayerState extends BasePlayerState {

	private static final int CHECK_PLAYER_PROGRESS_PERIODIC_INTERVAL_IN_MILLISECONDS = 1000;
	private PeriodicAction periodicAction;

	public StartedPlayerState(PlayerStateContext playerStateContext) {
		super(playerStateContext, true, true);
		this.periodicAction = new PeriodicAction(
				CHECK_PLAYER_PROGRESS_PERIODIC_INTERVAL_IN_MILLISECONDS,
				() -> getMediaPlayerSafely(value -> saveMediaPlayerStateAndNotify(playerToState(value))));
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
		denyDeviceSleep();
		this.periodicAction.start();
		this.playerStateContext
				.requestFocus(
						player::start,
						() -> {
							throw new FailRequestAudioFocusException();
						});
	}

	@Override
	public void unapply() {
		this.periodicAction.stop();
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
