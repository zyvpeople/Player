package com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces;

import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.PlayerSourceStrategy;

/**
 * User: zuzik
 * Date: 6/18/16
 */
public interface PlayerSourceStrategyFactory<SourceInfo> {
	PlayerSourceStrategy<SourceInfo> create(boolean shuffle);
}
