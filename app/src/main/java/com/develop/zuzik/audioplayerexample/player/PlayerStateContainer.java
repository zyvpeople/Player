package com.develop.zuzik.audioplayerexample.player;

import com.develop.zuzik.audioplayerexample.player.player_states.BasePlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.PlayerState;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public interface PlayerStateContainer {
	void setState(PlayerState state);
}
