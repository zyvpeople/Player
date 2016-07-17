package com.develop.zuzik.audioplayerexample.mvp.player.presenter_destroy_strategy;

import com.develop.zuzik.playermvp.interfaces.Player;

/**
 * User: zuzik
 * Date: 7/4/16
 */
public class DoNothingPlayerPresenterDestroyStrategy implements PlayerPresenterDestroyStrategy {
	@Override
	public void onDestroy(Player.Model model) {
	}
}
