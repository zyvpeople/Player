package com.develop.zuzik.audioplayerexample.mvp.implementations.presenters;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.Player;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.PlayerModelState;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.State;

import java.util.Arrays;
import java.util.List;

import rx.Subscription;

/**
 * User: zuzik
 * Date: 6/4/16
 */
//TODO: there is a lot of duplication between Player and MultiplePlayer Presenters
public class PlayerPresenter implements Player.Presenter {

	private final Player.Model model;
	private Player.View view;
	private Subscription playbackStateChangedSubscription;
	private Subscription errorPlayingSubscription;

	List<State> allowedPlayButtonStates = Arrays.asList(State.IDLE, State.PAUSED, State.COMPLETED);
	List<State> allowedPauseButtonStates = Arrays.asList(State.PLAYING);
	List<State> allowedStopButtonStates = Arrays.asList(State.PLAYING, State.PAUSED, State.COMPLETED);

	public PlayerPresenter(Player.Model model) {
		this.model = model;
	}

	@Override
	public void onInit(Player.View view) {
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
	public void simulateError() {
		this.model.simulateError();
	}

	private void updateView() {
		updateView(this.model.getState());
	}

	private void updateView(PlayerModelState state) {
		PlaybackState bundle = state.bundle;
		this.view.enablePlayControls(
				this.allowedPlayButtonStates.contains(bundle.state),
				this.allowedPauseButtonStates.contains(bundle.state),
				this.allowedStopButtonStates.contains(bundle.state));

		if (bundle.maxTimeInMilliseconds.isPresent()) {
			this.view.showTime(String.valueOf(bundle.currentTimeInMilliseconds), bundle.maxTimeInMilliseconds.transform(String::valueOf).get());
			this.view.showProgress();
			this.view.setProgress(bundle.currentTimeInMilliseconds, bundle.maxTimeInMilliseconds.get());
		} else {
			this.view.showTime("-", "-");
			this.view.hideProgress();
			this.view.setProgress(0, 100);
		}

		if (state.repeat) {
			this.view.setRepeat();
		} else {
			this.view.setDoNotRepeat();
		}

		if (bundle.state == State.PREPARING) {
			this.view.showLoading();
		} else {
			this.view.hideLoading();
		}
	}
}
