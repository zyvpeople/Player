package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;
import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.PlayerStateBundle;
import com.develop.zuzik.audioplayerexample.player.interfaces.PlayerStateContainer;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class IdlePlayerState extends BasePlayerState {

	public IdlePlayerState(MediaPlayer player, PlayerSource source, PlayerStateContainer stateContainer) {
		super(player, source, stateContainer);
	}

	@Override
	public PlayerStateBundle getPlayerStateBundle() {
		return new PlayerStateBundle(
				PlaybackState.IDLE,
				0,
				Optional.absent(),
				getPlayer().isLooping());
	}

	@Override
	public void setRepeat(boolean repeat) {
		getPlayer().setLooping(repeat);
	}

	@Override
	public void set(Context context) {
		super.set(context);
		onPlaybackStateChanged();
	}

	@Override
	public void play() {
		super.play();
		getPlayer().reset();
		setState(new PreparingPlayerState(getPlayer(), getSource(), getStateContainer()));
	}
}
