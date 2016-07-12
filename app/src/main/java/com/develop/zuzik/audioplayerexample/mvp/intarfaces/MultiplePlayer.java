package com.develop.zuzik.audioplayerexample.mvp.intarfaces;

import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.MultiplePlaybackState;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;

import java.util.List;

import rx.Observable;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public interface MultiplePlayer {
	interface Model<SourceInfo> {
		void setSources(List<PlayerSource<SourceInfo>> sources);

		void clear();

		MultiplePlaybackState<SourceInfo> getState();

		Observable<Void> stateChangedObservable();

		Observable<Throwable> errorPlayingObservable();

		void play();

		void pause();

		void stop();

		void skipNext();

		void skipPrevious();

		void seekToPosition(int positionInMilliseconds);

		void repeatSingle();

		void doNotRepeatSingle();

		void shuffle();

		void doNotShuffle();

		void switchToSource(PlayerSource<SourceInfo> source);

		void simulateError();
	}

	interface View<SourceInfo> {
		void repeat();

		void doNotRepeat();

		void shuffle();

		void doNotShuffle();

		void setProgress(int currentTimeInMilliseconds, int totalTimeInMilliseconds);

		void showProgress();

		void hideProgress();

		void showTime(String currentTime, String totalTime);

		void showError(String message);

		void enablePlayControls(boolean play, boolean pause, boolean stop);

		void displayCurrentSource(PlayerSource<SourceInfo> source);

		void doNotDisplayCurrentSource();

		void displaySources(List<PlayerSource<SourceInfo>> playerSources);
	}

	interface Presenter<SourceInfo> {
		void setView(View<SourceInfo> view);

		void onCreate();

		void onDestroy();

		void onAppear();

		void onDisappear();

		void onSetPlayerSources(List<PlayerSource<SourceInfo>> playerSources);

		void onPlay();

		void onPause();

		void onStop();

		void onSkipNext();

		void onSkipPrevious();

		void onSeekToPosition(int positionInMilliseconds);

		void onRepeatSingle();

		void onDoNotRepeatSingle();

		void onShuffle();

		void onDoNotShuffle();

		void onRepeatAll();

		void onDoNotRepeatAll();

		void onSwitchToSource(PlayerSource<SourceInfo> source);

		void simulateError();
	}
}
