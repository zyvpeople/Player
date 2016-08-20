package com.develop.zuzik.player.video_view_setter;

import android.support.annotation.Nullable;
import android.view.SurfaceHolder;

import com.develop.zuzik.player.interfaces.ParamAction;
import com.develop.zuzik.player.interfaces.VideoViewSetter;

/**
 * User: zuzik
 * Date: 8/14/16
 */
public class MediaPlayerVideoViewSetter implements VideoViewSetter {

	private final ParamAction<SurfaceHolder> action;

	public MediaPlayerVideoViewSetter(ParamAction<SurfaceHolder> action) {
		this.action = action;
	}

	@Override
	public void setVideoView(@Nullable SurfaceHolder holder) {
		this.action.execute(holder);
	}
}
