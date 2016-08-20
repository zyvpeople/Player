package com.develop.zuzik.multipleplayermvp.presenter;

import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackState;
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer;
import com.develop.zuzik.multipleplayermvp.null_object.NullMultiplePlayerVideoView;
import com.develop.zuzik.player.interfaces.ParamAction;
import com.develop.zuzik.player.interfaces.PlaybackState;
import com.develop.zuzik.player.interfaces.VideoViewSetter;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class MultiplePlayerVideoPresenter<SourceInfo> implements MultiplePlayer.VideoPresenter<SourceInfo> {

	private final MultiplePlayer.Model<SourceInfo> model;
	private final SourceInfo sourceInfo;
	private MultiplePlayer.VideoView<SourceInfo> view = NullMultiplePlayerVideoView.getInstance();
	private boolean appeared;

	public MultiplePlayerVideoPresenter(
			MultiplePlayer.Model<SourceInfo> model,
			SourceInfo sourceInfo) {
		this.model = model;
		this.sourceInfo = sourceInfo;
	}

	@Override
	public void setView(MultiplePlayer.VideoView<SourceInfo> view) {
		this.view = view != null ? view : NullMultiplePlayerVideoView.<SourceInfo>getInstance();
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onDestroy() {
		this.view = NullMultiplePlayerVideoView.getInstance();
	}

	@Override
	public void onAppear() {
		this.appeared = true;
		updateView();
		this.model.addListener(this.listener);
	}

	@Override
	public void onDisappear() {
		this.model.removeListener(this.listener);
		this.appeared = false;
		updateView();
	}

	@Override
	public void onVideoViewCreated() {
		updateView();
	}

	@Override
	public void onVideoViewDestroyed() {
		updateView();
	}

	private void updateView() {
		if (isCurrentSourceInfo()) {
			if (this.appeared) {
				this.view.setVideoViewAvailable();
				this.model.videoViewSetter(new ParamAction<VideoViewSetter>() {
					@Override
					public void execute(VideoViewSetter value) {
						MultiplePlayerVideoPresenter.this.view.setVideoView(value);
					}
				});
			} else {
				this.view.setVideoViewUnavailable();
				this.model.videoViewSetter(new ParamAction<VideoViewSetter>() {
					@Override
					public void execute(VideoViewSetter value) {
						MultiplePlayerVideoPresenter.this.view.clearVideoView(value);
					}
				});
			}
		} else {
			this.view.setVideoViewUnavailable();
		}
	}

	private boolean isCurrentSourceInfo() {
		if (this.model.getState().isPresent()) {
			MultiplePlaybackState<SourceInfo> modelState = this.model.getState().get();
			if (modelState.currentPlaybackState.isPresent()) {
				PlaybackState<SourceInfo> playbackState = modelState.currentPlaybackState.get();
				if (this.sourceInfo.equals(playbackState.playerSource.getSourceInfo())) {
					return true;
				}
			}
		}
		return false;
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
