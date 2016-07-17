package com.develop.zuzik.playermvp.presenter_destroy_strategy;

import com.develop.zuzik.playermvp.interfaces.Player;
import com.develop.zuzik.playermvp.interfaces.PlayerPresenterDestroyStrategy;

/**
 * User: zuzik
 * Date: 7/4/16
 */
public class ClearModelPlayerPresenterDestroyStrategy implements PlayerPresenterDestroyStrategy {
	@Override
	public void onDestroy(Player.Model model) {
		model.clear();
	}
}
