package com.develop.zuzik.audioplayerexample.player.interfaces;

import com.develop.zuzik.audioplayerexample.player.MultiplePlayerStateBundle;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public interface MultiplePlaybackListener {

	void onChange(MultiplePlayerStateBundle bundle);

	void onError();
}
