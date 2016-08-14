package com.develop.zuzik.playermvp.presenter;

import com.develop.zuzik.player.interfaces.PlaybackState;
import com.develop.zuzik.playermvp.interfaces.Player;
import com.develop.zuzik.playermvp.null_object.NullPlayerVideoView;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class PlayerVideoPresenter<SourceInfo> implements Player.VideoPresenter<SourceInfo> {

	private final Player.Model<SourceInfo> model;
	private final SourceInfo sourceInfo;
	private Player.VideoView<SourceInfo> view = NullPlayerVideoView.getInstance();
	private boolean appeared;

	public PlayerVideoPresenter(Player.Model<SourceInfo> model, SourceInfo sourceInfo) {
		this.model = model;
		this.sourceInfo = sourceInfo;
	}

	@Override
	public void setView(Player.VideoView<SourceInfo> view) {
		this.view = view != null
				? view
				: NullPlayerVideoView.getInstance();
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onDestroy() {
		this.view = NullPlayerVideoView.getInstance();
	}

	@Override
	public void onAppear() {
		this.appeared = true;
		updateView();
		this.model.addListener(this.modelListener);
	}

	@Override
	public void onDisappear() {
		this.model.removeListener(this.modelListener);
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
		updateView(this.model.getState());
	}

	private void updateView(Optional<PlaybackState<SourceInfo>> state) {
		if (isCurrentSourceInfo(state)) {
			if (this.appeared) {
				this.view.setVideoViewAvailable();
				this.model.videoViewSetter(value -> this.view.setVideoView(value));
			} else {
				this.view.setVideoViewUnavailable();
				this.model.videoViewSetter(value -> this.view.clearVideoView(value));
			}
		} else {
			this.view.setVideoViewUnavailable();
		}
	}

	private boolean isCurrentSourceInfo(Optional<PlaybackState<SourceInfo>> state) {
		if (this.model.getState().isPresent()) {
			PlaybackState<SourceInfo> playbackState = this.model.getState().get();
			if(this.sourceInfo.equals(playbackState.playerSource.getSourceInfo())){
				return true;
			}
		}
		return false;
	}

	private final Player.Model.Listener<SourceInfo> modelListener = new Player.Model.Listener<SourceInfo>() {
		@Override
		public void onUpdate(PlaybackState<SourceInfo> state) {
			updateView(Optional.of(state));
		}

		@Override
		public void onError(Throwable error) {
		}
	};
}
