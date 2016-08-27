package com.develop.zuzik.multipleplayer.local;

import android.content.Context;

import com.develop.zuzik.multipleplayer.interfaces.MultiplePlayback;
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackFactory;
import com.develop.zuzik.multipleplayer.interfaces.PlayerSourceReleaseStrategy;
import com.develop.zuzik.multipleplayer.interfaces.PlayerSourceDetermineStrategyFactory;
import com.develop.zuzik.player.interfaces.PlaybackFactory;

/**
 * User: zuzik
 * Date: 7/7/16
 */
public class LocalMultiplePlaybackFactory<SourceInfo> implements MultiplePlaybackFactory<SourceInfo> {

	private final PlaybackFactory<SourceInfo> playbackFactory;
	private final PlayerSourceDetermineStrategyFactory<SourceInfo> nextPlayerSourceDetermineStrategyFactory;
	private final PlayerSourceDetermineStrategyFactory<SourceInfo> previousPlayerSourceDetermineStrategyFactory;
	private final PlayerSourceDetermineStrategyFactory<SourceInfo> onCompletePlayerSourceDetermineStrategyFactory;
	private final boolean repeatSingle;
	private final boolean shuffle;

	public LocalMultiplePlaybackFactory(
			PlaybackFactory<SourceInfo> playbackFactory,
			PlayerSourceDetermineStrategyFactory<SourceInfo> nextPlayerSourceDetermineStrategyFactory,
			PlayerSourceDetermineStrategyFactory<SourceInfo> previousPlayerSourceDetermineStrategyFactory,
			PlayerSourceDetermineStrategyFactory<SourceInfo> onCompletePlayerSourceDetermineStrategyFactory,
			boolean repeatSingle,
			boolean shuffle) {
		this.playbackFactory = playbackFactory;
		this.nextPlayerSourceDetermineStrategyFactory = nextPlayerSourceDetermineStrategyFactory;
		this.previousPlayerSourceDetermineStrategyFactory = previousPlayerSourceDetermineStrategyFactory;
		this.onCompletePlayerSourceDetermineStrategyFactory = onCompletePlayerSourceDetermineStrategyFactory;
		this.repeatSingle = repeatSingle;
		this.shuffle = shuffle;
	}

	@Override
	public MultiplePlayback<SourceInfo> create(Context context) {
		return new LocalMultiplePlayback<>(
				context,
				this.playbackFactory,
				this.nextPlayerSourceDetermineStrategyFactory,
				this.previousPlayerSourceDetermineStrategyFactory,
				this.onCompletePlayerSourceDetermineStrategyFactory,
				this.repeatSingle,
				this.shuffle);
	}
}
