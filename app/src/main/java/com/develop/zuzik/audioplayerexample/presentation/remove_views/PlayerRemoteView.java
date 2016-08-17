package com.develop.zuzik.audioplayerexample.presentation.remove_views;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.develop.zuzik.audioplayerexample.R;
import com.develop.zuzik.audioplayerexample.domain.Song;
import com.develop.zuzik.audioplayerexample.presentation.appwidget_provider.PauseBroadcastReceiver;
import com.develop.zuzik.audioplayerexample.presentation.appwidget_provider.PlayBroadcastReceiver;
import com.develop.zuzik.audioplayerexample.presentation.appwidget_provider.StopBroadcastReceiver;

/**
 * User: zuzik
 * Date: 8/15/16
 */

public class PlayerRemoteView extends RemoteViews {

	public PlayerRemoteView(Context context) {
		super(context.getPackageName(), R.layout.widget_player);
	}

	public void setSong(Context context, Song song) {
		setTextViewText(R.id.tvTitle, song.artist);
		setTextViewText(R.id.tvSubtitle, song.name);
		setOnClickPendingIntent(R.id.btnPlay, PendingIntent.getBroadcast(context, 0, new Intent(context, PlayBroadcastReceiver.class), 0));
		setOnClickPendingIntent(R.id.btnPause, PendingIntent.getBroadcast(context, 0, new Intent(context, PauseBroadcastReceiver.class), 0));
		setOnClickPendingIntent(R.id.btnStop, PendingIntent.getBroadcast(context, 0, new Intent(context, StopBroadcastReceiver.class), 0));
	}
}
