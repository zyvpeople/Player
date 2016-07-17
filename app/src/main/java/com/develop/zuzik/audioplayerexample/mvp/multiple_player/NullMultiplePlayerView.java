package com.develop.zuzik.audioplayerexample.mvp.multiple_player;

import com.develop.zuzik.audioplayerexample.mvp.interfaces.MultiplePlayer;
import com.develop.zuzik.player.source.PlayerSource;

import java.util.List;

/**
 * User: zuzik
 * Date: 7/12/16
 */
public class NullMultiplePlayerView<SourceInfo> implements MultiplePlayer.View<SourceInfo> {
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
