package com.develop.zuzik.player.video;

import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.develop.zuzik.player.interfaces.VideoViewSetter;

/**
 * User: zuzik
 * Date: 8/13/16
 */
public class SurfaceViewWrapper {

	private final SurfaceHolder holder;
	private Strategy strategy = new UnavailableHolderStrategy();

	public SurfaceViewWrapper(SurfaceView view, final Listener listener) {
		this.holder = view.getHolder();
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

	public void setVideoView(VideoViewSetter setter) {
		this.strategy.setVideoView(setter);
	}

	public void clearVideoView(VideoViewSetter setter) {
		setter.setVideoView(null);
	}

	private interface Strategy {
		void setVideoView(VideoViewSetter setter);
	}

	private class AvailableHolderStrategy implements Strategy {
		@Override
		public void setVideoView(VideoViewSetter setter) {
			setter.setVideoView(holder);
		}
	}

	private class UnavailableHolderStrategy implements Strategy {
		@Override
		public void setVideoView(VideoViewSetter setter) {
		}
	}
}
