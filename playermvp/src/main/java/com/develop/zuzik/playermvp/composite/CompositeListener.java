package com.develop.zuzik.playermvp.composite;

import com.develop.zuzik.player.interfaces.PlaybackState;
import com.develop.zuzik.playermvp.interfaces.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zuzik
 * Date: 7/17/16
 */
public class CompositeListener<SourceInfo> implements Player.Model.Listener<SourceInfo> {

	private final List<Player.Model.Listener<SourceInfo>> listeners = new ArrayList<>();

	public void addListener(Player.Model.Listener<SourceInfo> listener) {
		if (!this.listeners.contains(listener)) {
			this.listeners.add(listener);
		}
	}

	public void removeListener(Player.Model.Listener<SourceInfo> listener) {
		this.listeners.remove(listener);
	}

	@Override
	public void onUpdate(PlaybackState<SourceInfo> state) {
		for (Player.Model.Listener<SourceInfo> listener : this.listeners) {
			listener.onUpdate(state);
		}
	}

	@Override
	public void onError(Throwable error) {
		for (Player.Model.Listener<SourceInfo> listener : this.listeners) {
			listener.onError(error);
		}
	}
}
