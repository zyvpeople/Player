package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;
import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.State;
import com.develop.zuzik.audioplayerexample.player.interfaces.PlaybackListener;
import com.develop.zuzik.audioplayerexample.player.interfaces.PlayerStateContainer;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerState;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateBundle;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class NullPlayerState implements PlayerState {

	@Override
	public PlayerStateBundle getPlayerStateBundle() {
		return new PlayerStateBundle(
				State.NONE,
				0,
				Optional.absent(),
				false);
	}

	@Override
	public void setPlayer(MediaPlayer player) {

	}

	@Override
	public void setPlayerSource(PlayerSource playerSource) {

	}

	@Override
	public void setPlayerStateContainer(PlayerStateContainer playerStateContainer) {

	}

	@Override
	public void setPlaybackListener(PlaybackListener playbackListener) {

	}

	@Override
	public void setRepeat(boolean repeat) {

	}

	@Override
	public void set(Context context) {

	}

	@Override
	public void unset() {

	}

	@Override
	public void play() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void stop() {

	}

	@Override
	public void simulateError() {

	}

	@Override
	public void seekTo(int positionInMilliseconds) {

	}

	@Override
	public void release() {

	}
}
