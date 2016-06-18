package com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.factories;

import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.EndedNextPlaybackStrategy;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.PlaybackStrategy;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.ShufflePlaybackStrategy;

/**
 * User: zuzik
 * Date: 6/18/16
 */
public class ExampleNextPlaybackStrategyFactory implements PlaybackStrategyFactory{
	@Override
	public PlaybackStrategy create(boolean shuffle) {
		return shuffle
				? new ShufflePlaybackStrategy()
				: new EndedNextPlaybackStrategy();
	}
}
