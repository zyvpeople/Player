package com.develop.zuzik.player.source;

import android.content.Context;
import android.media.MediaPlayer;

import com.develop.zuzik.player.device_sleep.DeviceSleep;
import com.develop.zuzik.player.exception.PlayerInitializeException;

import java.io.Serializable;

/**
 * User: zuzik
 * Date: 6/2/16
 */
public interface PlayerSource<SourceInfo> extends Serializable {
	SourceInfo getSourceInfo();

	void initializePlayerWithSource(Context context, MediaPlayer player) throws PlayerInitializeException;

	DeviceSleep createDeviceSleep(Context context);
}
