package com.develop.zuzik.player.device_sleep;

import android.content.Context;
import android.content.ContextWrapper;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.fernandocejas.arrow.functions.Function;
import com.fernandocejas.arrow.optional.Optional;

import org.jetbrains.annotations.Nullable;


/**
 * User: zuzik
 * Date: 7/17/16
 */
public class WiFiDeviceSleep implements DeviceSleep {

	private final Context context;
	private Optional<WifiManager.WifiLock> wifiLock = Optional.absent();

	public WiFiDeviceSleep(Context context) {
		this.context = new ContextWrapper(context).getApplicationContext();
	}

	@Override
	public void allow() {
		if (!this.wifiLock.isPresent()) {
			WifiManager wifiManager = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
			this.wifiLock = wifiManager != null
					? Optional.of(wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, getClass().getSimpleName()))
					: Optional.<WifiManager.WifiLock>absent();
		}
		if (!this.wifiLock.transform(new Function<WifiManager.WifiLock, Boolean>() {
			@Nullable
			@Override
			public Boolean apply(WifiManager.WifiLock input) {
				return input.isHeld();
			}
		}).or(true)) {
			this.wifiLock.get().acquire();
			Log.i(getClass().getSimpleName(), "allow");
		}
	}

	@Override
	public void deny() {
		if (this.wifiLock.transform(new Function<WifiManager.WifiLock, Boolean>() {
			@Nullable
			@Override
			public Boolean apply(WifiManager.WifiLock input) {
				return input.isHeld();
			}
		}).or(false)) {
			this.wifiLock.get().release();
			Log.i(getClass().getSimpleName(), "deny");
		}
	}
}
