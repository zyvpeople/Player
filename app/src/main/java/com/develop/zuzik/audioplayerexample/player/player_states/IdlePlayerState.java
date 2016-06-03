package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.develop.zuzik.audioplayerexample.player.PlaybackBundle;
import com.develop.zuzik.audioplayerexample.player.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.PlayerStateContainer;
import com.develop.zuzik.audioplayerexample.player.exceptions.PlayerInitializeException;
import com.develop.zuzik.audioplayerexample.player.player_initializer.PlayerInitializer;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class IdlePlayerState extends BasePlayerState {

	private boolean preparing;

	public IdlePlayerState(MediaPlayer player, PlayerInitializer initializer, PlayerStateContainer stateContainer) {
		super(player, initializer, stateContainer);
	}

	@Override
	public PlaybackBundle getPlaybackBundle() {
		return createBundle();
	}

	@Override
	public void setRepeat(boolean repeat) {
		if (!this.preparing) {
			getPlayer().setLooping(repeat);
		}
	}

	@Override
	public void set() {
		super.set();
		getPlayer().setOnPreparedListener(player -> {
			player.start();
			setState(new StartedPlayerState(player, getInitializer(), getStateContainer()));
		});
		onPlaybackStateChanged(createBundle());
	}

	@Override
	public void unset() {
		getPlayer().setOnPreparedListener(null);
		super.unset();
	}

	@Override
	public void play(Context context) {
		super.play(context);
		if (this.preparing) {
			return;
		}
		this.preparing = true;
		getPlayer().reset();
		try {
			onPlaybackStateChanged(createBundle());
			getInitializer().initialize(context, getPlayer());
			getPlayer().prepareAsync();
		} catch (PlayerInitializeException e) {
			Log.e(getClass().getSimpleName(), e.getMessage());
			handleError();
		}
	}

	private PlaybackBundle createBundle() {
		return new PlaybackBundle(this.preparing ? PlaybackState.PREPARING : PlaybackState.IDLE, 0, null, getPlayer().isLooping());
	}
}
