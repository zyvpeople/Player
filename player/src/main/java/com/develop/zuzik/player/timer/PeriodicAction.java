package com.develop.zuzik.player.timer;

import android.os.Handler;

import com.develop.zuzik.player.interfaces.Action;

/**
 * User: zuzik
 * Date: 8/20/16
 */
public class PeriodicAction {

	private final Handler handler = new Handler();
	private final int periodicIntervalInMilliseconds;
	private final Action action;

	public PeriodicAction(int periodicIntervalInMilliseconds, Action action) {
		this.periodicIntervalInMilliseconds = periodicIntervalInMilliseconds;
		this.action = action;
	}

	public void start() {
		stop();
		this.handler.postDelayed(this.runnable, this.periodicIntervalInMilliseconds);
	}

	public void stop() {
		this.handler.removeCallbacks(this.runnable);
	}

	private final Runnable runnable = new Runnable() {
		@Override
		public void run() {
			action.execute();
			start();
		}
	};
}
