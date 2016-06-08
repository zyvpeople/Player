package com.develop.zuzik.audioplayerexample.mvp.implementations.presenters;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.MultiplePlayer;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.MultiplePlayerModelState;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.MultiplePlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.State;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscription;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class MultiplePlayerPresenter implements MultiplePlayer.Presenter {

	private final MultiplePlayer.Model model;
	private MultiplePlayer.View view;
	private Subscription playbackStateChangedSubscription;
	private Subscription errorPlayingSubscription;

	List<State> allowedPlayButtonStates = Arrays.asList(State.IDLE, State.PAUSED, State.COMPLETED);
	List<State> allowedPauseButtonStates = Arrays.asList(State.PLAYING);
	List<State> allowedStopButtonStates = Arrays.asList(State.PLAYING, State.PAUSED, State.COMPLETED);

	public MultiplePlayerPresenter(MultiplePlayer.Model model) {
		this.model = model;
	}

	@Override
	public void onInit(MultiplePlayer.View view) {
		this.view = view;
		this.model.init();
	}

	@Override
	public void onDestroy() {
		this.view = null;
		this.model.destroy();
	}

	@Override
	public void onAppear() {
		updateView();
		this.playbackStateChangedSubscription = this.model.stateChangedObservable()
				.subscribe(aVoid -> updateView());
		this.errorPlayingSubscription = this.model.errorPlayingObservable()
				.subscribe(aVoid -> this.view.showError("Error playing song"));
	}

	@Override
	public void onDisappear() {
		this.playbackStateChangedSubscription.unsubscribe();
		this.errorPlayingSubscription.unsubscribe();
	}

	@Override
	public void onPlay() {
		this.model.play();
	}

	@Override
	public void onPause() {
		this.model.pause();
	}

	@Override
	public void onStop() {
		this.model.stop();
	}

	@Override
	public void onSkipNext() {
		this.model.skipNext();
	}

	@Override
	public void onSkipPrevious() {
		this.model.skipPrevious();
	}

	@Override
	public void onSeekToPosition(int positionInMilliseconds) {
		this.model.seekToPosition(positionInMilliseconds);
	}

	@Override
	public void onRepeat() {
		this.model.repeat();
	}

	@Override
	public void onDoNotRepeat() {
		this.model.doNotRepeat();
	}

	@Override
	public void onShuffle() {
		this.model.shuffle();
	}

	@Override
	public void onDoNotShuffle() {
		this.model.doNotShuffle();
	}

	@Override
	public void simulateError() {
		this.model.simulateError();
	}

	private void updateView() {
		updateView(this.model.getState());
	}

	private void updateView(MultiplePlayerModelState state) {
		if (state.repeat) {
			this.view.repeat();
		} else {
			this.view.doNotRepeat();
		}

		MultiplePlaybackState bundle = state.bundle;
		if (bundle.playerState.isPresent()) {
			PlaybackState playbackState = bundle.playerState.get();

			this.view.enablePlayControls(
					this.allowedPlayButtonStates.contains(playbackState.state),
					this.allowedPauseButtonStates.contains(playbackState.state),
					this.allowedStopButtonStates.contains(playbackState.state));

			if (playbackState.maxTimeInMilliseconds.isPresent()) {
				int currentTime = playbackState.currentTimeInMilliseconds;
				int maxTime = playbackState.maxTimeInMilliseconds.get();
				this.view.showTime(
						timeToRepresentation(currentTime),
						timeToRepresentation(maxTime));
				this.view.showProgress();
				this.view.setProgress(currentTime, maxTime);
			} else {
				this.view.showTime("", "");
				this.view.hideProgress();
				this.view.setProgress(0, 100);
			}
		} else {
			this.view.showTime("", "");
			this.view.hideProgress();
			this.view.setProgress(0, 100);
		}
	}

	private String timeToRepresentation(long milliseconds) {
		return String.format("%d:%02d",
				TimeUnit.MILLISECONDS.toMinutes(milliseconds),
				TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
						TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
	}
}
