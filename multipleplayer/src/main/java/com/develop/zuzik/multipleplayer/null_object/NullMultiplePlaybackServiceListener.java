package com.develop.zuzik.multipleplayer.null_object;

import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackServiceListener;

/**
 * User: zuzik
 * Date: 8/28/16
 */
public class NullMultiplePlaybackServiceListener implements MultiplePlaybackServiceListener {

	public static final NullMultiplePlaybackServiceListener INSTANCE = new NullMultiplePlaybackServiceListener();

	private NullMultiplePlaybackServiceListener() {
	}

	@Override
	public void onReceiveDestroyCommand() {

	}
}
