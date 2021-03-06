package com.develop.zuzik.multipleplayermvp.presenter;

import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer;
import com.develop.zuzik.multipleplayermvp.null_object.NullMultiplePlayerView;
import com.develop.zuzik.player.analyzer.PlaybackStateAnalyzer;
import com.develop.zuzik.player.interfaces.PlayerExceptionMessageProvider;
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayerPresenterDestroyStrategy;
import com.develop.zuzik.player.transformation.ExceptionToMessageTransformation;
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackState;
import com.develop.zuzik.player.interfaces.PlaybackState;
import com.develop.zuzik.player.interfaces.State;
import com.develop.zuzik.player.source.PlayerSource;
import com.fernandocejas.arrow.functions.Function;
import com.fernandocejas.arrow.optional.Optional;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class MultiplePlayerPresenter<SourceInfo> implements MultiplePlayer.Presenter<SourceInfo> {

	private final MultiplePlayer.Model<SourceInfo> model;
	private MultiplePlayer.View<SourceInfo> view = NullMultiplePlayerView.getInstance();
	private final ExceptionToMessageTransformation exceptionToMessageTransformation;

	private final MultiplePlayerPresenterDestroyStrategy destroyStrategy;

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
		this.view = view != null ? view : NullMultiplePlayerView.<SourceInfo>getInstance();
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onDestroy() {
		this.view = NullMultiplePlayerView.getInstance();
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

	private void updateView(Optional<MultiplePlaybackState<SourceInfo>> state) {
		List<PlayerSource<SourceInfo>> sources = new ArrayList<>();

		if (state.isPresent()) {
			sources = state.get().playerSources;
		}

		this.view.displaySources(sources);
		if (state.transform(new Function<MultiplePlaybackState<SourceInfo>, Boolean>() {
			@Nullable
			@Override
			public Boolean apply(MultiplePlaybackState<SourceInfo> input) {
				return input.currentPlaybackState.isPresent();
			}
		}).or(false)) {
			PlaybackState<SourceInfo> playbackState = state.get().currentPlaybackState.get();
			PlaybackStateAnalyzer<SourceInfo> analyzer = new PlaybackStateAnalyzer<>(playbackState);

			this.view.displayCurrentSource(playbackState.playerSource);
		} else {
			this.view.doNotDisplayCurrentSource();
		}
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
