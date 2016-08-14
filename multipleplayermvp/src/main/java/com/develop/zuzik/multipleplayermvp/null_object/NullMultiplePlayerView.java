package com.develop.zuzik.multipleplayermvp.null_object;

import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer;
import com.develop.zuzik.player.source.PlayerSource;

import java.util.List;

/**
 * User: zuzik
 * Date: 7/12/16
 */
public class NullMultiplePlayerView<SourceInfo> implements MultiplePlayer.View<SourceInfo> {

	private static final NullMultiplePlayerView INSTANCE = new NullMultiplePlayerView();

	public static <SourceInfo> NullMultiplePlayerView<SourceInfo> getInstance() {
		return INSTANCE;
	}

	private NullMultiplePlayerView() {
	}

	@Override
	public void repeat() {

	}

	@Override
	public void doNotRepeat() {

	}

	@Override
	public void shuffle() {

	}

	@Override
	public void doNotShuffle() {

	}

	@Override
	public void setProgress(int currentTimeInMilliseconds, int totalTimeInMilliseconds) {

	}

	@Override
	public void showProgress() {

	}

	@Override
	public void hideProgress() {

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
	public void displayCurrentSource(PlayerSource<SourceInfo> source) {

	}

	@Override
	public void doNotDisplayCurrentSource() {

	}

	@Override
	public void displaySources(List<PlayerSource<SourceInfo>> playerSources) {

	}

}
