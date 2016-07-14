package com.develop.zuzik.audioplayerexample.mvp.multiple_player.presenter_destroy_strategy;

import com.develop.zuzik.audioplayerexample.mvp.interfaces.MultiplePlayer;

/**
 * User: zuzik
 * Date: 7/4/16
 */
public class DoNothingMultiplePlayerPresenterDestroyStrategy implements MultiplePlayerPresenterDestroyStrategy {
	@Override
	public void onDestroy(MultiplePlayer.Model model) {
	}
}
