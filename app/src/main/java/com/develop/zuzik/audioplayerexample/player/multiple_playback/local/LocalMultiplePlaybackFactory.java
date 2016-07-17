package com.develop.zuzik.audioplayerexample.player.multiple_playback.local;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.MultiplePlayback;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.MultiplePlaybackFactory;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.PlayerSourceStrategy;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.PlayerSourceStrategyFactory;
import com.develop.zuzik.player.interfaces.PlaybackFactory;

/**
 * User: zuzik
 * Date: 7/7/16
 */
public class LocalMultiplePlaybackFactory<SourceInfo> implements MultiplePlaybackFactory<SourceInfo> {

	private final PlaybackFactory<SourceInfo> playbackFactory;
	private final PlayerSourceStrategy<SourceInfo> nextPlayerSourceStrategy;
	private final PlayerSourceStrategy<SourceInfo> previousPlayerSourceStrategy;
	private final PlayerSourceStrategyFactory<SourceInfo> onCompletePlayerSourceStrategyFactory;
	private final boolean repeatSingle;
	private final boolean shuffle;

	public LocalMultiplePlaybackFactory(
			PlaybackFactory<SourceInfo> playbackFactory,
			PlayerSourceStrategy<SourceInfo> nextPlayerSourceStrategy,
			PlayerSourceStrategy<SourceInfo> previousPlayerSourceStrategy,
			PlayerSourceStrategyFactory<SourceInfo> onCompletePlayerSourceStrategyFactory,
			boolean repeatSingle,
			boolean shuffle) {
		this.playbackFactory = playbackFactory;
		this.nextPlayerSourceStrategy = nextPlayerSourceStrategy;
		this.previousPlayerSourceStrategy = previousPlayerSourceStrategy;
		this.onCompletePlayerSourceStrategyFactory = onCompletePlayerSourceStrategyFactory;
		this.repeatSingle = repeatSingle;
		this.shuffle = shuffle;
	}

	@Override
	public MultiplePlayback<SourceInfo> create(Context context) {
		return new LocalMultiplePlayback<>(
				context,
				this.playbackFactory,
				this.nextPlayerSourceStrategy,
				this.previousPlayerSourceStrategy,
				this.onCompletePlayerSourceStrategyFactory,
				this.repeatSingle,
				this.shuffle);
	}
}
