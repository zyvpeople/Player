package com.develop.zuzik.audioplayerexample.domain;

import com.develop.zuzik.multipleplayermvp.interfaces.ControlAvailabilityStrategy;
import com.develop.zuzik.player.source.PlayerSource;

import java.util.List;

/**
 * User: zuzik
 * Date: 8/27/16
 */
public class ExampleNextControlAvailabilityStrategy implements ControlAvailabilityStrategy<Song> {
	@Override
	public boolean available(List<PlayerSource<Song>> playerSources, PlayerSource<Song> currentPlayerSource, boolean shuffle) {
		if (playerSources.isEmpty() || playerSources.size() == 1) {
			return false;
		}
		if (shuffle) {
			return true;
		}
		boolean lastSong = playerSources.indexOf(currentPlayerSource) == playerSources.size() - 1;
		if (lastSong) {
			return false;
		}
		return true;
	}
}
