package com.develop.zuzik.multipleplayer.service;

import android.content.Context;
import android.content.Intent;

import com.develop.zuzik.player.interfaces.Action;
import com.develop.zuzik.player.interfaces.ParamAction;
import com.develop.zuzik.player.source.PlayerSource;

/**
 * User: zuzik
 * Date: 6/20/16
 */
public class MultiplePlaybackServiceIntentFactory {

	private static final String EXTRA_MULTIPLE_PLAYBACK_SERVICE_INITIALIZE_BUNDLE = "EXTRA_MULTIPLE_PLAYBACK_SERVICE_INITIALIZE_BUNDLE";
	private static final String EXTRA_SEEK_TO = "EXTRA_SEEK_TO";
	private static final String EXTRA_SWITCH_TO_SOURCE = "EXTRA_SWITCH_TO_SOURCE";
	private static final String ACTION_INIT = "ACTION_INIT";
	private static final String ACTION_DESTROY = "ACTION_DESTROY";
	private static final String ACTION_PLAY = "ACTION_PLAY";
	private static final String ACTION_PAUSE = "ACTION_PAUSE";
	private static final String ACTION_STOP = "ACTION_STOP";
	private static final String ACTION_PLAY_NEXT = "ACTION_PLAY_NEXT";
	private static final String ACTION_PLAY_PREVIOUS = "ACTION_PLAY_PREVIOUS";
	private static final String ACTION_SEEK_TO = "ACTION_SEEK_TO";
	private static final String ACTION_SWITCH_TO_SOURCE = "ACTION_SWITCH_TO_SOURCE";
	private static final String ACTION_REPEAT_SINGLE = "ACTION_REPEAT_SINGLE";
	private static final String ACTION_DO_NOT_REPEAT_SINGLE = "ACTION_DO_NOT_REPEAT_SINGLE";
	private static final String ACTION_SHUFFLE = "ACTION_SHUFFLE";
	private static final String ACTION_DO_NOT_SHUFFLE = "ACTION_DO_NOT_SHUFFLE";
	private static final String ACTION_SIMULATE_ERROR = "ACTION_SIMULATE_ERROR";

	public static Intent create(Context context) {
		return new Intent(context, MultiplePlaybackService.class);
	}

	private static Intent createWithAction(Context context, String action) {
		return create(context).setAction(action);
	}

	public static Intent createForInit(Context context,
									   MultiplePlaybackServiceInitializeBundle bundle) {
		return createWithAction(context, ACTION_INIT)
				.putExtra(EXTRA_MULTIPLE_PLAYBACK_SERVICE_INITIALIZE_BUNDLE, bundle);
	}

	static void parseForInit(final Intent intent, final ParamAction<MultiplePlaybackServiceInitializeBundle> success) {
		parseAction(intent, ACTION_INIT, new Action() {
			@Override
			public void execute() {
				if (intent.hasExtra(EXTRA_MULTIPLE_PLAYBACK_SERVICE_INITIALIZE_BUNDLE)) {
					success.execute((MultiplePlaybackServiceInitializeBundle) intent.getSerializableExtra(EXTRA_MULTIPLE_PLAYBACK_SERVICE_INITIALIZE_BUNDLE));
				}
			}
		});
	}

	public static Intent createForDestroy(Context context) {
		return createWithAction(context, ACTION_DESTROY);
	}

	static void parseForDestroy(final Intent intent, final Action success) {
		parseAction(intent, ACTION_DESTROY, success);
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

	public static Intent createPlayNext(Context context) {
		return createWithAction(context, ACTION_PLAY_NEXT);
	}

	static void parsePlayNext(Intent intent, Action success) {
		parseAction(intent, ACTION_PLAY_NEXT, success);
	}

	public static Intent createPlayPrevious(Context context) {
		return createWithAction(context, ACTION_PLAY_PREVIOUS);
	}

	static void parsePlayPrevious(Intent intent, Action success) {
		parseAction(intent, ACTION_PLAY_PREVIOUS, success);
	}

	private static void parseAction(Intent intent, String action, Action success) {
		if (intent != null && action.equals(intent.getAction())) {
			success.execute();
		}
	}

	public static Intent createSeekTo(Context context, int positionInMilliseconds) {
		return createWithAction(context, ACTION_SEEK_TO).putExtra(EXTRA_SEEK_TO, positionInMilliseconds);
	}

	static void parseSeekTo(final Intent intent, final ParamAction<Integer> success) {
		parseAction(intent, ACTION_SEEK_TO, new Action() {
			@Override
			public void execute() {
				if (intent.hasExtra(EXTRA_SEEK_TO)) {
					success.execute(intent.getIntExtra(EXTRA_SEEK_TO, 0));
				}
			}
		});
	}

	public static Intent createSwitchToSource(Context context, PlayerSource source) {
		return createWithAction(context, ACTION_SWITCH_TO_SOURCE).putExtra(EXTRA_SWITCH_TO_SOURCE, source);
	}

	static void parseSwitchToSource(final Intent intent, final ParamAction<PlayerSource> success) {
		parseAction(intent, ACTION_SWITCH_TO_SOURCE, new Action() {
			@Override
			public void execute() {
				if (intent.hasExtra(EXTRA_SWITCH_TO_SOURCE)) {
					success.execute((PlayerSource) intent.getSerializableExtra(EXTRA_SWITCH_TO_SOURCE));
				}
			}
		});
	}

	public static Intent createRepeatSingle(Context context) {
		return createWithAction(context, ACTION_REPEAT_SINGLE);
	}

	static void parseRepeatSingle(Intent intent, Action success) {
		parseAction(intent, ACTION_REPEAT_SINGLE, success);
	}

	public static Intent createDoNotRepeatSingle(Context context) {
		return createWithAction(context, ACTION_DO_NOT_REPEAT_SINGLE);
	}

	static void parseDoNotRepeatSingle(Intent intent, Action success) {
		parseAction(intent, ACTION_DO_NOT_REPEAT_SINGLE, success);
	}

	public static Intent createShuffle(Context context) {
		return createWithAction(context, ACTION_SHUFFLE);
	}

	static void parseShuffle(Intent intent, Action success) {
		parseAction(intent, ACTION_SHUFFLE, success);
	}

	public static Intent createDoNotShuffle(Context context) {
		return createWithAction(context, ACTION_DO_NOT_SHUFFLE);
	}

	static void parseDoNotShuffle(Intent intent, Action success) {
		parseAction(intent, ACTION_DO_NOT_SHUFFLE, success);
	}

	public static Intent createSimulateError(Context context) {
		return createWithAction(context, ACTION_SIMULATE_ERROR);
	}

	static void parseSimulateError(Intent intent, Action success) {
		parseAction(intent, ACTION_SIMULATE_ERROR, success);
	}
}
