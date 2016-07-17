package com.develop.zuzik.multipleplayer.null_object;

import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackListener;
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackState;

/**
 * User: zuzik
 * Date: 7/7/16
 */
public class NullMultiplePlaybackListener<SourceInfo> implements MultiplePlaybackListener<SourceInfo> {

	private static final NullMultiplePlaybackListener INSTANCE = new NullMultiplePlaybackListener();

	public static <SourceInfo> NullMultiplePlaybackListener<SourceInfo> getInstance() {
		return INSTANCE;
	}

	private NullMultiplePlaybackListener() {
	}

	@Override
	public void onUpdate(MultiplePlaybackState<SourceInfo> multiplePlaybackState) {

	}

	@Override
	public void onError(Throwable throwable) {

	}
}
