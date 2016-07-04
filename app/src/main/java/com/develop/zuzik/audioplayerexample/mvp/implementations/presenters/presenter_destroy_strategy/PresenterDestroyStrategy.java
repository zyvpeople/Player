package com.develop.zuzik.audioplayerexample.mvp.implementations.presenters.presenter_destroy_strategy;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.Player;

/**
 * User: zuzik
 * Date: 7/4/16
 */
public interface PresenterDestroyStrategy {
	void onDestroy(Player.Model model);
}
