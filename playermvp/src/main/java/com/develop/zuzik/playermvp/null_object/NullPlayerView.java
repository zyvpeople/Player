package com.develop.zuzik.playermvp.null_object;

import com.develop.zuzik.playermvp.interfaces.Player;

/**
 * User: zuzik
 * Date: 6/22/16
 */
public class NullPlayerView<SourceInfo> implements Player.View<SourceInfo> {

	private static final NullPlayerView INSTANCE = new NullPlayerView();

	public static <SourceInfo> NullPlayerView<SourceInfo> getInstance() {
		return INSTANCE;
	}

	private NullPlayerView() {
	}

	@Override
	public void setRepeat() {

	}

	@Override
	public void setDoNotRepeat() {

	}

	@Override
	public void setProgress(int totalTimeInMilliseconds, int currentTimeInMilliseconds) {

	}

	@Override
	public void showSourceProgress() {

	}

	@Override
	public void hideSourceProgress() {

	}

	@Override
	public void showTime(String currentTime, String totalTime) {

	}

	@Override
	public void showError(String message) {

	}

	@Override
	public void enablePlayControls(boolean play, boolean pause, boolean stop) {

	}

	@Override
	public void showLoadingIndicator() {

	}

	@Override
	public void hideLoadingIndicator() {

	}

	@Override
	public void displayCurrentSource(SourceInfo sourceInfo) {

	}

	@Override
	public void doNotDisplayCurrentSource() {

	}

}
