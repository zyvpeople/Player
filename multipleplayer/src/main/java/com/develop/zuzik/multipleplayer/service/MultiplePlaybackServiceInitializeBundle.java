package com.develop.zuzik.multipleplayer.service;

import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackFactory;
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlayerNotificationFactory;
import com.develop.zuzik.player.source.PlayerSource;

import java.io.Serializable;
import java.util.List;

/**
 * User: zuzik
 * Date: 7/6/16
 */
public class MultiplePlaybackServiceInitializeBundle<SourceInfo> implements Serializable {
	public final MultiplePlaybackFactory<SourceInfo> multiplePlaybackFactory;
	public final List<PlayerSource<SourceInfo>> playerSources;
	public final int notificationId;
	public final MultiplePlayerNotificationFactory<SourceInfo> multiplePlayerNotificationFactory;

	public MultiplePlaybackServiceInitializeBundle(
			MultiplePlaybackFactory<SourceInfo> multiplePlaybackFactory,
			List<PlayerSource<SourceInfo>> playerSources,
			int notificationId,
			MultiplePlayerNotificationFactory<SourceInfo> multiplePlayerNotificationFactory) {
		this.multiplePlaybackFactory = multiplePlaybackFactory;
		this.playerSources = playerSources;
		this.notificationId = notificationId;
		this.multiplePlayerNotificationFactory = multiplePlayerNotificationFactory;
	}
}
