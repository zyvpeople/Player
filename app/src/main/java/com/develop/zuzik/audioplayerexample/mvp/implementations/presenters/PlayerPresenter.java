package com.develop.zuzik.audioplayerexample.mvp.implementations.presenters;

import com.develop.zuzik.audioplayerexample.mvp.implementations.presenters.presenter_destroy_strategy.PresenterDestroyStrategy;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.Player;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.PlayerExceptionMessageProvider;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.null_objects.NullPlayerView;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.State;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

import java.util.Arrays;
import java.util.List;

import rx.internal.util.SubscriptionList;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class PlayerPresenter<SourceInfo> implements Player.Presenter<SourceInfo> {

	private final Player.Model<SourceInfo> model;
	private Player.View<SourceInfo> view = new NullPlayerView<>();
	private final ExceptionToMessageTransformation exceptionToMessageTransformation;
	private final PresenterDestroyStrategy destroyStrategy;
	private final SubscriptionList subscriptions = new SubscriptionList();

	private List<State> allowedPlayButtonStates = Arrays.asList(State.IDLE, State.PAUSED, State.COMPLETED);
	private List<State> allowedPauseButtonStates = Arrays.asList(State.PLAYING);
	private List<State> allowedStopButtonStates = Arrays.asList(State.PLAYING, State.PAUSED, State.COMPLETED);

	public PlayerPresenter(Player.Model<SourceInfo> model, PlayerExceptionMessageProvider exceptionMessageProvider, PresenterDestroyStrategy destroyStrategy) {
		this.model = model;
		this.exceptionToMessageTransformation = new ExceptionToMessageTransformation(exceptionMessageProvider);
		this.destroyStrategy = destroyStrategy;
	}

	@Override
	public void setView(Player.View<SourceInfo> view) {
		this.view = view != null
				? view
				: new NullPlayerView<>();
	}

	@Override
	public void onCreated() {
	}

	@Override
	public void onDestroy() {
		this.destroyStrategy.onDestroy(this.model);
	}

	@Override
	public void onAppear() {
		updateView();
		this.subscriptions.add(this.model.updateObservable()
				.map(Optional::of)
				.subscribe(this::updateView));
		this.subscriptions.add(this.model.errorObservable()
				.map(this.exceptionToMessageTransformation::transform)
				.subscribe(this.view::showError));
	}

	@Override
	public void onDisappear() {
		this.subscriptions.unsubscribe();
		this.subscriptions.clear();
	}

	@Override
	public void onSetSource(PlayerSource<SourceInfo> source) {
		this.model.setSource(source);
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

	private void updateView(Optional<PlaybackState<SourceInfo>> state) {

		boolean repeat = false;
		int currentTimeInMilliseconds = 0;
		int totalTimeInMilliseconds = 100;
		boolean showSourceProgress = false;
		boolean playAvailable = false;
		boolean pauseAvailable = false;
		boolean stopAvailable = false;
		boolean showLoading = false;
		Optional<SourceInfo> sourceInfo = Optional.absent();
		String currentTimeText = "";
		String totalTimeText = "";

		if (state.isPresent()) {
			PlaybackState<SourceInfo> bundle = state.get();
			repeat = bundle.repeat;
			currentTimeInMilliseconds = bundle.currentTimeInMilliseconds;
			totalTimeInMilliseconds = bundle.maxTimeInMilliseconds.or(totalTimeInMilliseconds);
			showSourceProgress = bundle.maxTimeInMilliseconds.isPresent();
			playAvailable = this.allowedPlayButtonStates.contains(bundle.state);
			pauseAvailable = this.allowedPauseButtonStates.contains(bundle.state);
			stopAvailable = this.allowedStopButtonStates.contains(bundle.state);
			showLoading = bundle.state == State.PREPARING;
			sourceInfo = Optional.of(bundle.playerSource.getSourceInfo());
			currentTimeText = bundle.maxTimeInMilliseconds.isPresent() ? String.valueOf(bundle.currentTimeInMilliseconds) : currentTimeText;
			totalTimeText = bundle.maxTimeInMilliseconds.transform(String::valueOf).or(totalTimeText);
		}

		this.view.enablePlayControls(playAvailable, pauseAvailable, stopAvailable);
		this.view.showTime(currentTimeText, totalTimeText);
		this.view.setProgress(currentTimeInMilliseconds, totalTimeInMilliseconds);

		if (showSourceProgress) {
			this.view.showSourceProgress();
		} else {
			this.view.hideSourceProgress();
		}

		if (repeat) {
			this.view.setRepeat();
		} else {
			this.view.setDoNotRepeat();
		}

		if (showLoading) {
			this.view.showLoadingIndicator();
		} else {
			this.view.hideLoadingIndicator();
		}

		if (sourceInfo.isPresent()) {
			this.view.displayCurrentSource(sourceInfo.get());
		} else {
			this.view.doNotDisplayCurrentSource();
		}
	}

}
