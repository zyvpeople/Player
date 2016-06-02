package com.develop.zuzik.audioplayerexample.player;

import com.develop.zuzik.audioplayerexample.player.player_states.BasePlayerState;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public interface PlayerStateContainer {
	void setState(BasePlayerState state);
}
