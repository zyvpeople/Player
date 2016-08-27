package com.develop.zuzik.multipleplayermvp.presenter;

import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackState;
import com.develop.zuzik.multipleplayermvp.interfaces.ControlAvailabilityStrategy;
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer;
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayerPresenterDestroyStrategy;
import com.develop.zuzik.multipleplayermvp.null_object.NullMultiplePlayerControlView;
import com.develop.zuzik.multipleplayermvp.null_object.NullMultiplePlayerView;
import com.develop.zuzik.player.analyzer.PlaybackStateAnalyzer;
import com.develop.zuzik.player.interfaces.PlaybackState;
import com.develop.zuzik.player.interfaces.PlayerExceptionMessageProvider;
import com.develop.zuzik.player.source.PlayerSource;
import com.develop.zuzik.player.transformation.ExceptionToMessageTransformation;
import com.fernandocejas.arrow.functions.Function;
import com.fernandocejas.arrow.optional.Optional;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class MultiplePlayerControlPresenter<SourceInfo> implements MultiplePlayer.ControlPresenter<SourceInfo> {

	private final MultiplePlayer.Model<SourceInfo> model;
	private final ControlAvailabilityStrategy<SourceInfo> nextControlAvailabilityStrategy;
	private final ControlAvailabilityStrategy<SourceInfo> previousControlAvailabilityStrategy;
	private MultiplePlayer.ControlView<SourceInfo> view = NullMultiplePlayerControlView.getInstance();

	public MultiplePlayerControlPresenter(
			MultiplePlayer.Model<SourceInfo> model,
			ControlAvailabilityStrategy<SourceInfo> nextControlAvailabilityStrategy,
			ControlAvailabilityStrategy<SourceInfo> previousControlAvailabilityStrategy) {
		this.model = model;
		this.nextControlAvailabilityStrategy = nextControlAvailabilityStrategy;
		this.previousControlAvailabilityStrategy = previousControlAvailabilityStrategy;
	}

	@Override
	public void setView(MultiplePlayer.ControlView<SourceInfo> view) {
		this.view = view != null ? view : NullMultiplePlayerControlView.<SourceInfo>getInstance();
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onDestroy() {
		this.view = NullMultiplePlayerControlView.getInstance();
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
		this.model.repeatSingle();
	}

	@Override
	public void onDoNotRepeat() {
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

	private void updateView() {
		updateView(this.model.getState());
	}

	private void updateView(Optional<MultiplePlaybackState<SourceInfo>> state) {
		boolean repeatSingle = false;
		boolean shuffle = false;

		if (state.isPresent()) {
			repeatSingle = state.get().repeatSingle;
			shuffle = state.get().shuffle;
		}

		if (repeatSingle) {
			this.view.repeat();
		} else {
			this.view.doNotRepeat();
		}

		if (shuffle) {
			this.view.shuffle();
		} else {
			this.view.doNotShuffle();
		}

		if (state.transform(new Function<MultiplePlaybackState<SourceInfo>, Boolean>() {
			@Nullable
			@Override
			public Boolean apply(MultiplePlaybackState<SourceInfo> input) {
				return input.currentPlaybackState.isPresent();
			}
		}).or(false)) {
			PlaybackState<SourceInfo> playbackState = state.get().currentPlaybackState.get();
			PlaybackStateAnalyzer<SourceInfo> analyzer = new PlaybackStateAnalyzer<>(playbackState);

			this.view.enablePlayControls(
					analyzer.playAvailable(),
					analyzer.pauseAvailable(),
					analyzer.stopAvailable());
			this.view.enableSwitchControls(
					this.nextControlAvailabilityStrategy.available(state.get().playerSources, state.get().currentPlaybackState.get().playerSource, state.get().shuffle),
					this.previousControlAvailabilityStrategy.available(state.get().playerSources, state.get().currentPlaybackState.get().playerSource, state.get().shuffle));

			if (playbackState.maxTimeInMilliseconds.isPresent()) {
				int currentTime = playbackState.currentTimeInMilliseconds;
				int maxTime = playbackState.maxTimeInMilliseconds.get();
				this.view.showProgress();
				this.view.setProgress(currentTime, maxTime);
			} else {
				this.view.hideProgress();
				this.view.setProgress(0, 100);
			}
		} else {
			this.view.enablePlayControls(false, false, false);
			this.view.enableSwitchControls(false, false);

			this.view.hideProgress();
			this.view.setProgress(0, 100);
		}
	}

	private final MultiplePlayer.Model.Listener<SourceInfo> listener = new MultiplePlayer.Model.Listener<SourceInfo>() {
		@Override
		public void onUpdate(MultiplePlaybackState state) {
			updateView();
		}

		@Override
		public void onError(Throwable error) {
		}
	};
}
