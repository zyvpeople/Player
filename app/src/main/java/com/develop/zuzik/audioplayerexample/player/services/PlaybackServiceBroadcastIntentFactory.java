package com.develop.zuzik.audioplayerexample.player.services;

import android.content.Intent;

import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.ParamAction;

import java.io.Serializable;

/**
 * User: zuzik
 * Date: 6/20/16
 */
public class PlaybackServiceBroadcastIntentFactory {

	public static final String ACTION_PLAYBACK_STATE = "ACTION_PLAYBACK_STATE";
	public static final String ACTION_ERROR = "ACTION_ERROR";
	private static final String EXTRA_PLAYBACK_STATE = "EXTRA_PLAYBACK_STATE";
	private static final String EXTRA_ERROR = "EXTRA_ERROR";

	public static Intent createPlaybackState(PlaybackState state) {
		Intent intent = new Intent(ACTION_PLAYBACK_STATE);
		intent.putExtra(EXTRA_PLAYBACK_STATE, (Serializable) state);
		return intent;
	}

	public static void parsePlaybackState(Intent intent, ParamAction<PlaybackState> success) {
		if (ACTION_PLAYBACK_STATE.equals(intent.getAction())) {
			success.execute((PlaybackState) intent.getSerializableExtra(EXTRA_PLAYBACK_STATE));
		}
	}

	public static Intent createError(Throwable throwable) {
		Intent intent = new Intent(ACTION_ERROR);
		intent.putExtra(EXTRA_ERROR, throwable);
		return intent;
	}

	public static void parseError(Intent intent, ParamAction<Throwable> success) {
		if (ACTION_ERROR.equals(intent.getAction())) {
			success.execute((Throwable) intent.getSerializableExtra(EXTRA_ERROR));
		}
	}
}
