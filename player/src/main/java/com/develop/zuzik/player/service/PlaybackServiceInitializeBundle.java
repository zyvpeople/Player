package com.develop.zuzik.player.service;

import com.develop.zuzik.player.interfaces.PlayerNotificationFactory;
import com.develop.zuzik.player.interfaces.PlaybackFactory;
import com.develop.zuzik.player.source.PlayerSource;

import java.io.Serializable;

/**
 * User: zuzik
 * Date: 7/6/16
 */
public class PlaybackServiceInitializeBundle<SourceInfo> implements Serializable {
	public final PlayerSource<SourceInfo> playerSource;
	public final PlaybackFactory<SourceInfo> playbackFactory;
	public final boolean repeat;
	public final int notificationId;
	public final PlayerNotificationFactory<SourceInfo> playerNotificationFactory;

	public PlaybackServiceInitializeBundle(PlayerSource<SourceInfo> playerSource,
										   PlaybackFactory<SourceInfo> playbackFactory,
										   boolean repeat,
										   int notificationId,
										   PlayerNotificationFactory<SourceInfo> playerNotificationFactory) {
		this.playerSource = playerSource;
		this.playbackFactory = playbackFactory;
		this.repeat = repeat;
		this.notificationId = notificationId;
		this.playerNotificationFactory = playerNotificationFactory;
	}
}
