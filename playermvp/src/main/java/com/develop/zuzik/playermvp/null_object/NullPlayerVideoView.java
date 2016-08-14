package com.develop.zuzik.playermvp.null_object;

import com.develop.zuzik.player.interfaces.VideoViewSetter;
import com.develop.zuzik.playermvp.interfaces.Player;

/**
 * User: zuzik
 * Date: 6/22/16
 */
public class NullPlayerVideoView<SourceInfo> implements Player.VideoView<SourceInfo> {

	private static final NullPlayerVideoView INSTANCE = new NullPlayerVideoView();

	public static <SourceInfo> NullPlayerVideoView<SourceInfo> getInstance() {
		return INSTANCE;
	}

	private NullPlayerVideoView() {
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
