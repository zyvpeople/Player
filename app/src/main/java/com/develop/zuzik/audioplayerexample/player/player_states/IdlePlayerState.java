package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.develop.zuzik.audioplayerexample.player.OnGetMaxDurationListener;
import com.develop.zuzik.audioplayerexample.player.OnSeekListener;
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
	public void set(OnGetMaxDurationListener onGetMaxDurationListener, OnSeekListener onSeekListener) {
		super.set(onGetMaxDurationListener, onSeekListener);
		getPlayer().setOnPreparedListener(player -> {
			player.start();
			int duration = player.getDuration();
			if (duration != -1) {
				getOnGetMaxDurationListener().onGetMaxDuration(duration);
			}
			setState(new StartedPlayerState(player, getInitializer(), getStateContainer()));
		});
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
			getInitializer().initialize(context, getPlayer());
			getPlayer().prepareAsync();
		} catch (PlayerInitializeException e) {
			Log.e(getClass().getSimpleName(), e.getMessage());
			setState(new ErrorPlayerState(getPlayer(), getInitializer(), getStateContainer()));
		}
	}
}
