package com.develop.zuzik.multipleplayermvp.presenter;

import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackState;
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer;
import com.develop.zuzik.multipleplayermvp.null_object.NullMultiplePlayerHidingView;
import com.develop.zuzik.multipleplayermvp.null_object.NullMultiplePlayerVideoView;
import com.develop.zuzik.player.interfaces.ParamAction;
import com.develop.zuzik.player.interfaces.PlaybackState;
import com.develop.zuzik.player.interfaces.VideoViewSetter;
import com.fernandocejas.arrow.functions.Function;

import org.jetbrains.annotations.Nullable;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class MultiplePlayerHidingPresenter<SourceInfo> implements MultiplePlayer.HidingPresenter<SourceInfo> {

	private final MultiplePlayer.Model<SourceInfo> model;
	private MultiplePlayer.HidingView<SourceInfo> view = NullMultiplePlayerHidingView.getInstance();

	public MultiplePlayerHidingPresenter(MultiplePlayer.Model<SourceInfo> model) {
		this.model = model;
	}

	@Override
	public void setView(MultiplePlayer.HidingView<SourceInfo> view) {
		this.view = view != null ? view : NullMultiplePlayerHidingView.<SourceInfo>getInstance();
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onDestroy() {
		this.view = NullMultiplePlayerHidingView.getInstance();
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
		boolean hasSources = this.model.getState()
				.transform(new Function<MultiplePlaybackState<SourceInfo>, Boolean>() {
					@Nullable
					@Override
					public Boolean apply(MultiplePlaybackState<SourceInfo> input) {
						return !input.playerSources.isEmpty();
					}
				})
				.or(false);
		if (hasSources) {
			this.view.displayPlayerView();
		} else {
			this.view.doNotDisplayPlayerView();
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
