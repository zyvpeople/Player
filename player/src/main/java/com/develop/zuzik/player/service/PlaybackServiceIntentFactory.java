package com.develop.zuzik.player.service;

import android.content.Context;
import android.content.Intent;

import com.develop.zuzik.player.interfaces.Action;
import com.develop.zuzik.player.interfaces.ParamAction;

/**
 * User: zuzik
 * Date: 6/20/16
 */
public class PlaybackServiceIntentFactory {

	private static final String EXTRA_PLAYBACK_SERVICE_INITIALIZE_BUNDLE = "EXTRA_PLAYBACK_SERVICE_INITIALIZE_BUNDLE";
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

	private static Intent createWithAction(Context context, String action) {
		return new Intent(context, PlaybackService.class).setAction(action);
	}

	public static Intent createForInit(Context context,
									   PlaybackServiceInitializeBundle bundle) {
		return createWithAction(context, ACTION_INIT)
				.putExtra(EXTRA_PLAYBACK_SERVICE_INITIALIZE_BUNDLE, bundle);
	}

	static void parseForInit(Intent intent, ParamAction<PlaybackServiceInitializeBundle> success) {
		parseAction(intent, ACTION_INIT, () -> {
			if (intent.hasExtra(EXTRA_PLAYBACK_SERVICE_INITIALIZE_BUNDLE)) {
				success.execute((PlaybackServiceInitializeBundle) intent.getSerializableExtra(EXTRA_PLAYBACK_SERVICE_INITIALIZE_BUNDLE));
			}
		});
	}

	public static Intent createPlay(Context context) {
		return createWithAction(context, ACTION_PLAY);
	}

	static void parsePlay(Intent intent, Action success) {
		parseAction(intent, ACTION_PLAY, success);
	}

	public static Intent createPause(Context context) {
		return createWithAction(context, ACTION_PAUSE);
	}

	static void parsePause(Intent intent, Action success) {
		parseAction(intent, ACTION_PAUSE, success);
	}

	public static Intent createStop(Context context) {
		return createWithAction(context, ACTION_STOP);
	}

	static void parseStop(Intent intent, Action success) {
		parseAction(intent, ACTION_STOP, success);
	}

	private static void parseAction(Intent intent, String action, Action success) {
		if (intent != null && action.equals(intent.getAction())) {
			success.execute();
		}
	}

	public static Intent createSeekTo(Context context, int positionInMilliseconds) {
		return createWithAction(context, ACTION_SEEK_TO).putExtra(EXTRA_SEEK_TO, positionInMilliseconds);
	}

	static void parseSeekTo(Intent intent, ParamAction<Integer> success) {
		parseAction(intent, ACTION_SEEK_TO, () -> {
			if (intent.hasExtra(EXTRA_SEEK_TO)) {
				success.execute(intent.getIntExtra(EXTRA_SEEK_TO, 0));
			}
		});
	}

	public static Intent createRepeat(Context context) {
		return createWithAction(context, ACTION_REPEAT);
	}

	static void parseRepeat(Intent intent, Action success) {
		parseAction(intent, ACTION_REPEAT, success);
	}

	public static Intent createDoNotRepeat(Context context) {
		return createWithAction(context, ACTION_DO_NOT_REPEAT);
	}

	static void parseDoNotRepeat(Intent intent, Action success) {
		parseAction(intent, ACTION_DO_NOT_REPEAT, success);
	}

	public static Intent createSimulateError(Context context) {
		return createWithAction(context, ACTION_SIMULATE_ERROR);
	}

	static void parseSimulateError(Intent intent, Action success) {
		parseAction(intent, ACTION_SIMULATE_ERROR, success);
	}
}
