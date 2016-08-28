package com.develop.zuzik.multipleplayermvp.interfaces;

/**
 * User: zuzik
 * Date: 7/4/16
 */
public interface MultiplePlayerPresenterDestroyStrategy<SourceInfo> {
	void onDestroy(MultiplePlayer.Model<SourceInfo> model);
}
