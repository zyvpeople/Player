package com.develop.zuzik.audioplayerexample.mvp.multiple_player.presenter_destroy_strategy;

import com.develop.zuzik.audioplayerexample.mvp.interfaces.MultiplePlayer;
import com.develop.zuzik.audioplayerexample.mvp.interfaces.Player;

/**
 * User: zuzik
 * Date: 7/4/16
 */
public interface MultiplePlayerPresenterDestroyStrategy {
	void onDestroy(MultiplePlayer.Model model);
}
