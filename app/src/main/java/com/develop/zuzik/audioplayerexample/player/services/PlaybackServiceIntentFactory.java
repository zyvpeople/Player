package com.develop.zuzik.audioplayerexample.player.services;

import android.content.Context;
import android.content.Intent;

import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.Action;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.ParamAction;

/**
 * User: zuzik
 * Date: 6/20/16
 */
public class PlaybackServiceIntentFactory {

	private static final String EXTRA_ACTION = "EXTRA_ACTION";
	private static final String EXTRA_PLAYER_SOURCE = "EXTRA_PLAYER_SOURCE";
	private static final String EXTRA_SEEK_TO = "EXTRA_SEEK_TO";
	private static final String ACTION_INIT = "ACTION_INIT";
	private static final String ACTION_PLAY = "ACTION_PLAY";
	private static final String ACTION_PAUSE = "ACTION_PAUSE";
	private static final String ACTION_STOP = "ACTION_STOP";
	private static final String ACTION_SEEK_TO = "ACTION_SEEK_TO";
	private static final String ACTION_REPEAT = "ACTION_REPEAT";
	private static final String ACTION_DO_NOT_REPEAT = "ACTION_DO_NOT_REPEAT";
	private static final String ACTION_SIMULATE_ERROR = "ACTION_SIMULATE_ERROR";

	public static Intent create(Context context) {
		return new Intent(context, PlaybackService.class);
	}

	public static Intent createPlayerSource(Context context, PlayerSource playerSource) {
		Intent intent = create(context);
		intent.putExtra(EXTRA_ACTION, ACTION_INIT);
		intent.putExtra(EXTRA_PLAYER_SOURCE, playerSource);
		return intent;
	}

	public static void parsePlayerSource(Intent intent, ParamAction<PlayerSource> success) {
		parseAction(intent, ACTION_INIT, () -> {
			if (intent != null && intent.hasExtra(EXTRA_PLAYER_SOURCE)) {
				success.execute((PlayerSource) intent.getSerializableExtra(EXTRA_PLAYER_SOURCE));
			}
		});
	}

	public static Intent createPlay(Context context) {
		Intent intent = create(context);
		intent.putExtra(EXTRA_ACTION, ACTION_PLAY);
		return intent;
	}

	public static void parsePlay(Intent intent, Action success) {
		parseAction(intent, ACTION_PLAY, success);
	}

	public static Intent createPause(Context context) {
		Intent intent = create(context);
		intent.putExtra(EXTRA_ACTION, ACTION_PAUSE);
		return intent;
	}

	public static void parsePause(Intent intent, Action success) {
		parseAction(intent, ACTION_PAUSE, success);
	}

	public static Intent createStop(Context context) {
		Intent intent = create(context);
		intent.putExtra(EXTRA_ACTION, ACTION_STOP);
		return intent;
	}

	public static void parseStop(Intent intent, Action success) {
		parseAction(intent, ACTION_STOP, success);
	}

	public static void parseAction(Intent intent, String action, Action success) {
		if (intent != null && action.equals(intent.getStringExtra(EXTRA_ACTION))) {
			success.execute();
		}
	}

	public static Intent createSeekTo(Context context, int positionInMilliseconds) {
		Intent intent = create(context);
		intent.putExtra(EXTRA_ACTION, ACTION_SEEK_TO);
		intent.putExtra(EXTRA_SEEK_TO, positionInMilliseconds);
		return intent;
	}

	public static void parseSeekTo(Intent intent, ParamAction<Integer> success) {
		parseAction(intent, ACTION_SEEK_TO, () -> {
			if (intent != null && intent.hasExtra(EXTRA_SEEK_TO)) {
				success.execute(intent.getIntExtra(EXTRA_SEEK_TO, 0));
			}
		});
	}

	public static Intent createRepeat(Context context) {
		Intent intent = create(context);
		intent.putExtra(EXTRA_ACTION, ACTION_REPEAT);
		return intent;
	}

	public static void parseRepeat(Intent intent, Action success) {
		parseAction(intent, ACTION_REPEAT, success);
	}

	public static Intent createDoNotRepeat(Context context) {
		Intent intent = create(context);
		intent.putExtra(EXTRA_ACTION, ACTION_DO_NOT_REPEAT);
		return intent;
	}

	public static void parseDoNotRepeat(Intent intent, Action success) {
		parseAction(intent, ACTION_DO_NOT_REPEAT, success);
	}

	public static Intent createSimulateError(Context context) {
		Intent intent = create(context);
		intent.putExtra(EXTRA_ACTION, ACTION_SIMULATE_ERROR);
		return intent;
	}

	public static void parseSimulateError(Intent intent, Action success) {
		parseAction(intent, ACTION_SIMULATE_ERROR, success);
	}
}
