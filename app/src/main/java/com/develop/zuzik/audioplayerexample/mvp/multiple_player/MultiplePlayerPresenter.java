package com.develop.zuzik.audioplayerexample.mvp.multiple_player;

import com.develop.zuzik.audioplayerexample.mvp.interfaces.MultiplePlayer;
import com.develop.zuzik.audioplayerexample.mvp.interfaces.PlayerExceptionMessageProvider;
import com.develop.zuzik.audioplayerexample.mvp.multiple_player.presenter_destroy_strategy.MultiplePlayerPresenterDestroyStrategy;
import com.develop.zuzik.audioplayerexample.mvp.player.ExceptionToMessageTransformation;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.MultiplePlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.State;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class MultiplePlayerPresenter<SourceInfo> implements MultiplePlayer.Presenter<SourceInfo> {

	private final MultiplePlayer.Model<SourceInfo> model;
	private MultiplePlayer.View<SourceInfo> view = new NullMultiplePlayerView<>();
	private final ExceptionToMessageTransformation exceptionToMessageTransformation;

	private final List<State> allowedPlayButtonStates = Arrays.asList(State.IDLE, State.PAUSED, State.COMPLETED);
	private final List<State> allowedPauseButtonStates = Arrays.asList(State.PLAYING);
	private final List<State> allowedStopButtonStates = Arrays.asList(State.PLAYING, State.PAUSED, State.COMPLETED);
	private MultiplePlayerPresenterDestroyStrategy destroyStrategy;

	public MultiplePlayerPresenter(
			MultiplePlayer.Model<SourceInfo> model,
			MultiplePlayerPresenterDestroyStrategy destroyStrategy,
			PlayerExceptionMessageProvider exceptionMessageProvider) {
		this.model = model;
		this.destroyStrategy = destroyStrategy;
		this.exceptionToMessageTransformation = new ExceptionToMessageTransformation(exceptionMessageProvider);
	}

	@Override
	public void setView(MultiplePlayer.View<SourceInfo> view) {
		this.view = view != null ? view : new NullMultiplePlayerView<>();
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onDestroy() {
		//TODO: set null view???
		this.destroyStrategy.onDestroy(this.model);
	}

	@Override
	public void onAppear() {
		updateView();
		this.model.addListener(this.listener);
	}

	@Override
	public void onDisappear() {
		this.model.removeListener(this.listener);
	}

	@Override
	public void onSetPlayerSources(List<PlayerSource<SourceInfo>> playerSources) {
		this.model.setSources(playerSources);
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
		//TODO: implement
	}

	@Override
	public void onDoNotRepeatAll() {
		//TODO: implement
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

	private void updateView(MultiplePlaybackState<SourceInfo> state) {
		if (state.repeatSingle) {
			this.view.repeat();
		} else {
			this.view.doNotRepeat();
		}

		if (state.shuffle) {
			this.view.shuffle();
		} else {
			this.view.doNotShuffle();
		}

		this.view.displaySources(state.playerSources);
		if (state.currentPlaybackState.isPresent()) {
			PlaybackState<SourceInfo> playbackState = state.currentPlaybackState.get();

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

	private final MultiplePlayer.Model.Listener<SourceInfo> listener = new MultiplePlayer.Model.Listener<SourceInfo>() {
		@Override
		public void onUpdate(MultiplePlaybackState<SourceInfo> state) {
			updateView();
		}

		@Override
		public void onError(Throwable error) {
			view.showError(exceptionToMessageTransformation.transform(error));
		}
	};
}
