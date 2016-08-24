package com.develop.zuzik.multipleplayermvp.presenter;

import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackState;
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer;
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayerPresenterDestroyStrategy;
import com.develop.zuzik.multipleplayermvp.null_object.NullMultiplePlayerActiveSourceView;
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

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class MultiplePlayerActiveSourcePresenter<SourceInfo> implements MultiplePlayer.ActiveSourcePresenter<SourceInfo> {

	private final MultiplePlayer.Model<SourceInfo> model;
	private final PlayerSource<SourceInfo> playerSource;
	private MultiplePlayer.ActiveSourceView<SourceInfo> view = NullMultiplePlayerActiveSourceView.getInstance();

	public MultiplePlayerActiveSourcePresenter(MultiplePlayer.Model<SourceInfo> model, PlayerSource<SourceInfo> playerSource) {
		this.model = model;
		this.playerSource = playerSource;
	}

	@Override
	public void setView(MultiplePlayer.ActiveSourceView<SourceInfo> view) {
		this.view = view != null ? view : NullMultiplePlayerActiveSourceView.<SourceInfo>getInstance();
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onDestroy() {
		this.view = NullMultiplePlayerActiveSourceView.getInstance();
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

	private void updateView() {
		updateView(this.model.getState());
	}

	private void updateView(Optional<MultiplePlaybackState<SourceInfo>> state) {
		if (state.transform(new Function<MultiplePlaybackState<SourceInfo>, Boolean>() {
			@Nullable
			@Override
			public Boolean apply(MultiplePlaybackState<SourceInfo> input) {
				return input.currentPlaybackState.isPresent();
			}
		}).or(false)) {
			if (state.get().currentPlaybackState.get().playerSource.equals(this.playerSource)) {
				PlaybackState<SourceInfo> playbackState = state.get().currentPlaybackState.get();
				this.view.displayAsActiveSource();
				if (playbackState.maxTimeInMilliseconds.isPresent()) {
					int currentTime = playbackState.currentTimeInMilliseconds;
					int maxTime = playbackState.maxTimeInMilliseconds.get();
					this.view.setProgress(currentTime, maxTime);
				} else {
					this.view.setProgress(0, 100);
				}
			} else {
				this.view.displayAsInactiveSource();
			}
		} else {
			this.view.displayAsInactiveSource();
		}
	}

	private final MultiplePlayer.Model.Listener<SourceInfo> listener = new MultiplePlayer.Model.Listener<SourceInfo>() {
		@Override
		public void onUpdate(MultiplePlaybackState<SourceInfo> state) {
			updateView();
		}

		@Override
		public void onError(Throwable error) {
		}
	};
}
