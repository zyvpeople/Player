package com.develop.zuzik.multipleplayermvp.presenter;

import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackState;
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer;
import com.develop.zuzik.multipleplayermvp.null_object.NullMultiplePlayerVideoView;
import com.develop.zuzik.player.interfaces.PlaybackState;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class MultiplePlayerVideoPresenter<SourceInfo> implements MultiplePlayer.VideoPresenter<SourceInfo> {

	private final MultiplePlayer.Model<SourceInfo> model;
	private final SourceInfo sourceInfo;
	private MultiplePlayer.VideoView<SourceInfo> view = NullMultiplePlayerVideoView.getInstance();

	public MultiplePlayerVideoPresenter(
			MultiplePlayer.Model<SourceInfo> model,
			SourceInfo sourceInfo) {
		this.model = model;
		this.sourceInfo = sourceInfo;
	}

	@Override
	public void setView(MultiplePlayer.VideoView<SourceInfo> view) {
		this.view = view != null ? view : NullMultiplePlayerVideoView.getInstance();
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
		updateView();
		this.model.addListener(this.listener);
	}

	@Override
	public void onDisappear() {
		this.model.removeListener(this.listener);
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
		if (this.model.getState().isPresent()) {
			MultiplePlaybackState<SourceInfo> modelState = this.model.getState().get();
			if(modelState.currentPlaybackState.isPresent()){
				PlaybackState<SourceInfo> playbackState = modelState.currentPlaybackState.get();
				if(this.sourceInfo.equals(playbackState.playerSource.getSourceInfo())){
					this.model.videoViewSetter(value -> this.view.setVideoView(value));
					this.view.setVideoViewAvailable();
					return;
				}
			}
		}
		this.view.setVideoViewUnavailable();
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
