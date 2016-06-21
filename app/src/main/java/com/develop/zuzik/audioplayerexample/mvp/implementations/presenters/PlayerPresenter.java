package com.develop.zuzik.audioplayerexample.mvp.implementations.presenters;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.Player;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.PlayerExceptionMessageProvider;
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
public class PlayerPresenter<SourceInfo> implements Player.Presenter<SourceInfo> {

	private final Player.Model<SourceInfo> model;
	private Player.View<SourceInfo> view;
	private final ExceptionToMessageTransformation exceptionToMessageTransformation;
	private Subscription playbackStateChangedSubscription;
	private Subscription errorPlayingSubscription;

	List<State> allowedPlayButtonStates = Arrays.asList(State.IDLE, State.PAUSED, State.COMPLETED);
	List<State> allowedPauseButtonStates = Arrays.asList(State.PLAYING);
	List<State> allowedStopButtonStates = Arrays.asList(State.PLAYING, State.PAUSED, State.COMPLETED);

	public PlayerPresenter(Player.Model<SourceInfo> model, PlayerExceptionMessageProvider exceptionMessageProvider) {
		this.model = model;
		this.exceptionToMessageTransformation = new ExceptionToMessageTransformation(exceptionMessageProvider);
	}

	@Override
	public void onInit(Player.View<SourceInfo> view) {
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
				.map(this.exceptionToMessageTransformation::transform)
				.subscribe(this.view::showError);
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

	private void updateView(PlayerModelState<SourceInfo> state) {
		PlaybackState<SourceInfo> bundle = state.bundle;

		Player.View.ViewData<SourceInfo> viewData = new Player.View.ViewData<>(
				state.repeat,
				bundle.currentTimeInMilliseconds,
				bundle.maxTimeInMilliseconds.or(100),
				bundle.maxTimeInMilliseconds.isPresent() ? String.valueOf(bundle.currentTimeInMilliseconds) : "-",
				bundle.maxTimeInMilliseconds.isPresent() ? bundle.maxTimeInMilliseconds.transform(String::valueOf).get() : "-",
				bundle.maxTimeInMilliseconds.isPresent(),
				this.allowedPlayButtonStates.contains(bundle.state),
				this.allowedPauseButtonStates.contains(bundle.state),
				this.allowedStopButtonStates.contains(bundle.state),
				bundle.state == State.PREPARING,
				bundle.playerSource.getSourceInfo());

		this.view.enablePlayControls(viewData.play, viewData.pause, viewData.stop);
		this.view.showTime(viewData.displayedCurrentTime, viewData.displayedTotalTime);
		this.view.setProgress(viewData.currentTimeInMilliseconds, viewData.totalTimeInMilliseconds);

		if (viewData.progressVisible) {
			this.view.showSourceProgress();
		} else {
			this.view.hideSourceProgress();
		}

		if (viewData.repeat) {
			this.view.setRepeat();
		} else {
			this.view.setDoNotRepeat();
		}

		if (viewData.loading) {
			this.view.showLoadingIndicator();
		} else {
			this.view.hideLoadingIndicator();
		}

		this.view.displayCurrentSource(bundle.playerSource.getSourceInfo());
		this.view.display(viewData);
	}

}
