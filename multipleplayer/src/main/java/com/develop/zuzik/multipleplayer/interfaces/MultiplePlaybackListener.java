package com.develop.zuzik.multipleplayer.interfaces;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public interface MultiplePlaybackListener<SourceInfo> {

	void onUpdate(MultiplePlaybackState<SourceInfo> multiplePlaybackState);

	void onError(Throwable throwable);
}
