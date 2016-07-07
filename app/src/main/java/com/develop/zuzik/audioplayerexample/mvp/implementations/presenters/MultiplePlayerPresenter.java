package com.develop.zuzik.audioplayerexample.mvp.implementations.presenters;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.MultiplePlayer;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.MultiplePlayerModelState;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.PlayerExceptionMessageProvider;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.MultiplePlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.State;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import rx.Subscription;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class MultiplePlayerPresenter<SourceInfo> implements MultiplePlayer.Presenter<SourceInfo> {

	private final MultiplePlayer.Model<SourceInfo> model;
	private MultiplePlayer.View<SourceInfo> view;
	private final ExceptionToMessageTransformation exceptionToMessageTransformation;
	private Subscription playbackStateChangedSubscription;
	private Subscription errorPlayingSubscription;

	private final List<State> allowedPlayButtonStates = Arrays.asList(State.IDLE, State.PAUSED, State.COMPLETED);
	private final List<State> allowedPauseButtonStates = Arrays.asList(State.PLAYING);
	private final List<State> allowedStopButtonStates = Arrays.asList(State.PLAYING, State.PAUSED, State.COMPLETED);

	public MultiplePlayerPresenter(MultiplePlayer.Model<SourceInfo> model, PlayerExceptionMessageProvider exceptionMessageProvider) {
		this.model = model;
		this.exceptionToMessageTransformation = new ExceptionToMessageTransformation(exceptionMessageProvider);
	}

	@Override
	public void onInit(MultiplePlayer.View<SourceInfo> view) {
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
	public void onRepeatSingle() {
		this.model.repeatSingle();
	}

	@Override
	public void onDoNotRepeatSingle() {
		this.model.doNotRepeatSingle();
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
	public void onRepeatAll() {

	}

	@Override
	public void onDoNotRepeatAll() {

	}

	@Override
	public void onSwitchToSource(PlayerSource<SourceInfo> source) {
		this.model.switchToSource(source);
	}

	@Override
	public void simulateError() {
		this.model.simulateError();
	}

	private void updateView() {
		updateView(this.model.getState());
	}

	private void updateView(MultiplePlayerModelState<SourceInfo> state) {
		if (state.repeat) {
			this.view.repeat();
		} else {
			this.view.doNotRepeat();
		}

		if (state.shuffle) {
			this.view.shuffle();
		} else {
			this.view.doNotShuffle();
		}

		MultiplePlaybackState<SourceInfo> bundle = state.bundle;
		this.view.displaySources(bundle.playerSources);
		if (bundle.currentPlaybackState.isPresent()) {
			PlaybackState<SourceInfo> playbackState = bundle.currentPlaybackState.get();

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
			this.view.displayCurrentSource(playbackState.playerSource);
		} else {
			this.view.showTime("", "");
			this.view.hideProgress();
			this.view.setProgress(0, 100);
			this.view.doNotDisplayCurrentSource();
		}
	}

	private String timeToRepresentation(long milliseconds) {
		return String.format(Locale.getDefault(),
				"%d:%02d",
				TimeUnit.MILLISECONDS.toMinutes(milliseconds),
				TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
						TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
	}
}
