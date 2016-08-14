package com.develop.zuzik.multipleplayermvp.null_object;

import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer;
import com.develop.zuzik.player.interfaces.VideoViewSetter;

/**
 * User: zuzik
 * Date: 7/12/16
 */
public class NullMultiplePlayerVideoView<SourceInfo> implements MultiplePlayer.VideoView<SourceInfo> {

	private static final NullMultiplePlayerVideoView INSTANCE = new NullMultiplePlayerVideoView();

	public static <SourceInfo> NullMultiplePlayerVideoView<SourceInfo> getInstance() {
		return INSTANCE;
	}

	private NullMultiplePlayerVideoView() {
	}

	@Override
	public void setVideoViewAvailable() {

	}

	@Override
	public void setVideoViewUnavailable() {

	}

	@Override
	public void setVideoView(VideoViewSetter setter) {

	}

	@Override
	public void clearVideoView(VideoViewSetter setter) {

	}
}
