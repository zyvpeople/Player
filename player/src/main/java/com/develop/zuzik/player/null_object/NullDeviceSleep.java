package com.develop.zuzik.player.null_object;

import com.develop.zuzik.player.device_sleep.DeviceSleep;

/**
 * User: zuzik
 * Date: 7/17/16
 */
public class NullDeviceSleep implements DeviceSleep {

	public static final NullDeviceSleep INSTANCE = new NullDeviceSleep();

	private NullDeviceSleep() {
	}

	@Override
	public void allow() {
	}

	@Override
	public void deny() {
	}
}
