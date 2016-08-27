package com.develop.zuzik.audioplayerexample.domain;

import com.develop.zuzik.multipleplayermvp.interfaces.ControlAvailabilityStrategy;
import com.develop.zuzik.player.source.PlayerSource;

import java.util.List;

/**
 * User: zuzik
 * Date: 8/27/16
 */
public class ExamplePreviousControlAvailabilityStrategy implements ControlAvailabilityStrategy<Song> {
	@Override
	public boolean available(List<PlayerSource<Song>> playerSources, PlayerSource<Song> currentPlayerSource, boolean shuffle) {
		if (playerSources.isEmpty() || playerSources.size() == 1) {
			return false;
		}
		if (shuffle) {
			return true;
		}
		boolean firstSong = playerSources.indexOf(currentPlayerSource) == 0;
		if (firstSong) {
			return false;
		}
		return true;
	}
}
