package com.develop.zuzik.audioplayerexample.mvp.multiple_player.presenter_destroy_strategy;

import com.develop.zuzik.audioplayerexample.mvp.MultiplePlayer;

/**
 * User: zuzik
 * Date: 7/4/16
 */
public class ClearModelMultiplePlayerPresenterDestroyStrategy implements MultiplePlayerPresenterDestroyStrategy {
	@Override
	public void onDestroy(MultiplePlayer.Model model) {
		model.clear();
	}
}
