package com.develop.zuzik.multipleplayermvp.null_object;

import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer;
import com.develop.zuzik.player.interfaces.VideoViewSetter;

/**
 * User: zuzik
 * Date: 7/12/16
 */
public class NullMultiplePlayerHidingView<SourceInfo> implements MultiplePlayer.HidingView<SourceInfo> {

	private static final NullMultiplePlayerHidingView INSTANCE = new NullMultiplePlayerHidingView();

	public static <SourceInfo> NullMultiplePlayerHidingView<SourceInfo> getInstance() {
		return INSTANCE;
	}

	private NullMultiplePlayerHidingView() {
	}

	@Override
	public void displayPlayerView() {

	}

	@Override
	public void doNotDisplayPlayerView() {

	}
}
