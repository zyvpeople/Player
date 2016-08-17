package com.develop.zuzik.audioplayerexample.presentation.appwidget_provider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.develop.zuzik.audioplayerexample.application.App;

/**
 * User: zuzik
 * Date: 8/17/16
 */
public class PlayBroadcastReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		((App)context.getApplicationContext()).getModel().play();
	}
}
