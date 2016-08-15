package com.develop.zuzik.audioplayerexample.presentation.remove_views;

import android.content.Context;
import android.widget.RemoteViews;

import com.develop.zuzik.audioplayerexample.R;
import com.develop.zuzik.audioplayerexample.domain.Song;

/**
 * User: zuzik
 * Date: 8/15/16
 */

public class PlayerRemoteView extends RemoteViews {

	public PlayerRemoteView(Context context) {
		super(context.getPackageName(), R.layout.widget_player);
	}

	public void setSong(Song song) {
		setTextViewText(R.id.tvTitle, song.artist);
		setTextViewText(R.id.tvSubtitle, song.name);
	}
}
