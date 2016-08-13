package com.develop.zuzik.player.video;

import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * User: zuzik
 * Date: 8/13/16
 */
public class SurfaceViewWrapper {

	private final SurfaceHolder holder;
	private final Listener listener;
	private Strategy strategy = new UnavailableHolderStrategy();

	public SurfaceViewWrapper(SurfaceView view, Listener listener) {
		this.holder = view.getHolder();
		this.listener = listener;
		this.holder.addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder surfaceHolder) {
				strategy = new AvailableHolderStrategy();
				listener.onCreated();
			}

			@Override
			public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
				strategy = new UnavailableHolderStrategy();
				listener.onDestroyed();
			}
		});
	}

	public void setVideoView(MediaPlayer player) {
		this.strategy.setVideoView(player);
	}

	public void clearVideoView(MediaPlayer player) {
		player.setDisplay(null);
	}

	private interface Strategy {
		void setVideoView(MediaPlayer player);
	}

	private class AvailableHolderStrategy implements Strategy {
		@Override
		public void setVideoView(MediaPlayer player) {
			player.setDisplay(holder);
		}
	}

	private class UnavailableHolderStrategy implements Strategy {
		@Override
		public void setVideoView(MediaPlayer player) {
		}
	}
}
