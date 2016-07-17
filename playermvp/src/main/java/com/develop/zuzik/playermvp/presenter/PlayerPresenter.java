package com.develop.zuzik.playermvp.presenter;

import com.develop.zuzik.player.transformation.ExceptionToMessageTransformation;
import com.develop.zuzik.playermvp.interfaces.Player;
import com.develop.zuzik.player.interfaces.PlayerExceptionMessageProvider;
import com.develop.zuzik.playermvp.interfaces.PlayerPresenterDestroyStrategy;
import com.develop.zuzik.player.interfaces.PlaybackState;
import com.develop.zuzik.player.interfaces.State;
import com.develop.zuzik.player.source.PlayerSource;
import com.develop.zuzik.playermvp.null_object.NullPlayerView;
import com.fernandocejas.arrow.optional.Optional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class PlayerPresenter<SourceInfo> implements Player.Presenter<SourceInfo> {

	private final Player.Model<SourceInfo> model;
	private Player.View<SourceInfo> view = NullPlayerView.getInstance();
	private final ExceptionToMessageTransformation exceptionToMessageTransformation;
	private final PlayerPresenterDestroyStrategy destroyStrategy;

	private final List<State> allowedPlayButtonStates = Arrays.asList(State.IDLE, State.PAUSED, State.COMPLETED);
	private final List<State> allowedPauseButtonStates = Collections.singletonList(State.PLAYING);
	private final List<State> allowedStopButtonStates = Arrays.asList(State.PLAYING, State.PAUSED, State.COMPLETED);

	public PlayerPresenter(Player.Model<SourceInfo> model, PlayerExceptionMessageProvider exceptionMessageProvider, PlayerPresenterDestroyStrategy destroyStrategy) {
		this.model = model;
		this.exceptionToMessageTransformation = new ExceptionToMessageTransformation(exceptionMessageProvider);
		this.destroyStrategy = destroyStrategy;
	}

	@Override
	public void setView(Player.View<SourceInfo> view) {
		this.view = view != null
				? view
				: NullPlayerView.getInstance();
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onDestroy() {
		this.view = NullPlayerView.getInstance();
		this.destroyStrategy.onDestroy(this.model);
	}

	@Override
	public void onAppear() {
		updateView();
		this.model.addListener(this.modelListener);
	}

	@Override
	public void onDisappear() {
		this.model.removeListener(this.modelListener);
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
		this.view.setProgress(totalTimeInMilliseconds, currentTimeInMilliseconds);

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

	private final Player.Model.Listener<SourceInfo> modelListener = new Player.Model.Listener<SourceInfo>() {
		@Override
		public void onUpdate(PlaybackState<SourceInfo> state) {
			updateView(Optional.of(state));
		}

		@Override
		public void onError(Throwable error) {
			view.showError(exceptionToMessageTransformation.transform(error));
		}
	};
}
