package com.develop.zuzik.audioplayerexample.mvp.intarfaces.null_objects;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.Player;

/**
 * User: zuzik
 * Date: 6/22/16
 */
public class NullPlayerView<SourceInfo> implements Player.View<SourceInfo> {
	@Override
	public void setRepeat() {

	}

	@Override
	public void setDoNotRepeat() {

	}

	@Override
	public void setProgress(int currentTimeInMilliseconds, int totalTimeInMilliseconds) {

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
