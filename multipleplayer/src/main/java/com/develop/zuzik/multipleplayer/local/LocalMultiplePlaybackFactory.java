package com.develop.zuzik.multipleplayer.local;

import android.content.Context;

import com.develop.zuzik.multipleplayer.interfaces.MultiplePlayback;
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackFactory;
import com.develop.zuzik.multipleplayer.interfaces.PlayerSourceStrategyFactory;
import com.develop.zuzik.player.interfaces.PlaybackFactory;

/**
 * User: zuzik
 * Date: 7/7/16
 */
public class LocalMultiplePlaybackFactory<SourceInfo> implements MultiplePlaybackFactory<SourceInfo> {

	private final PlaybackFactory<SourceInfo> playbackFactory;
	private final PlayerSourceStrategyFactory<SourceInfo> nextPlayerSourceStrategyFactory;
	private final PlayerSourceStrategyFactory<SourceInfo> previousPlayerSourceStrategyFactory;
	private final PlayerSourceStrategyFactory<SourceInfo> onCompletePlayerSourceStrategyFactory;
	private final boolean repeatSingle;
	private final boolean shuffle;

	public LocalMultiplePlaybackFactory(
			PlaybackFactory<SourceInfo> playbackFactory,
			PlayerSourceStrategyFactory<SourceInfo> nextPlayerSourceStrategyFactory,
			PlayerSourceStrategyFactory<SourceInfo> previousPlayerSourceStrategyFactory,
			PlayerSourceStrategyFactory<SourceInfo> onCompletePlayerSourceStrategyFactory,
			boolean repeatSingle,
			boolean shuffle) {
		this.playbackFactory = playbackFactory;
		this.nextPlayerSourceStrategyFactory = nextPlayerSourceStrategyFactory;
		this.previousPlayerSourceStrategyFactory = previousPlayerSourceStrategyFactory;
		this.onCompletePlayerSourceStrategyFactory = onCompletePlayerSourceStrategyFactory;
		this.repeatSingle = repeatSingle;
		this.shuffle = shuffle;
	}

	@Override
	public MultiplePlayback<SourceInfo> create(Context context) {
		return new LocalMultiplePlayback<>(
				context,
				this.playbackFactory,
				this.nextPlayerSourceStrategyFactory,
				this.previousPlayerSourceStrategyFactory,
				this.onCompletePlayerSourceStrategyFactory,
				this.repeatSingle,
				this.shuffle);
	}
}
