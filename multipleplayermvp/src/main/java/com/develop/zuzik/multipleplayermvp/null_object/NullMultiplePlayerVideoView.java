package com.develop.zuzik.multipleplayermvp.null_object;

import android.media.MediaPlayer;

import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer;

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
	public void setVideoView(MediaPlayer player) {

	}

	@Override
	public void clearVideoView(MediaPlayer player) {

	}
}
