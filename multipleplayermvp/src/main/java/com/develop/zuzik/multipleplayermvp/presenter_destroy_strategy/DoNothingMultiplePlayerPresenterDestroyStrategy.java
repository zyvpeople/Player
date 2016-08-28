package com.develop.zuzik.multipleplayermvp.presenter_destroy_strategy;

import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer;
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayerPresenterDestroyStrategy;

/**
 * User: zuzik
 * Date: 7/4/16
 */
public class DoNothingMultiplePlayerPresenterDestroyStrategy<SourceInfo> implements MultiplePlayerPresenterDestroyStrategy<SourceInfo> {
	@Override
	public void onDestroy(MultiplePlayer.Model<SourceInfo> model) {
		
	}
}
