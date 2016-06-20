package com.develop.zuzik.audioplayerexample.mvp.intarfaces;

import rx.Observable;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public interface Player {
	interface Model<SourceInfo> {
		void init();

		void destroy();

		PlayerModelState<SourceInfo> getState();

		Observable<Void> stateChangedObservable();

		Observable<Throwable> errorPlayingObservable();

		void play();

		void pause();

		void stop();

		void seekToPosition(int positionInMilliseconds);

		void repeat();

		void doNotRepeat();

		void simulateError();
	}

	interface View<SourceInfo> {
		void setRepeat();

		void setDoNotRepeat();

		void setProgress(int currentTimeInMilliseconds, int totalTimeInMilliseconds);

		//TODO: rename because showLoading exists
		void showProgress();

		void hideProgress();

		void showTime(String currentTime, String totalTime);

		void showError(String message);

		void enablePlayControls(boolean play, boolean pause, boolean stop);

		void showLoading();

		void hideLoading();

		void displayCurrentSource(SourceInfo sourceInfo);

		void display(ViewData<SourceInfo> viewData);

		class ViewData<SourceInfo> {
			public final boolean repeat;
			public final int currentTimeInMilliseconds;
			public final int totalTimeInMilliseconds;
			public final String displayedCurrentTime;
			public final String displayedTotalTime;
			public final boolean progressVisible;
			public final boolean play;
			public final boolean pause;
			public final boolean stop;
			public final boolean loading;
			public final SourceInfo sourceInfo;

			public ViewData(
					boolean repeat,
					int currentTimeInMilliseconds,
					int totalTimeInMilliseconds,
					String displayedCurrentTime,
					String displayedTotalTime,
					boolean progressVisible,
					boolean play,
					boolean pause,
					boolean stop,
					boolean loading,
					SourceInfo sourceInfo) {
				this.repeat = repeat;
				this.currentTimeInMilliseconds = currentTimeInMilliseconds;
				this.totalTimeInMilliseconds = totalTimeInMilliseconds;
				this.displayedCurrentTime = displayedCurrentTime;
				this.displayedTotalTime = displayedTotalTime;
				this.progressVisible = progressVisible;
				this.play = play;
				this.pause = pause;
				this.stop = stop;
				this.loading = loading;
				this.sourceInfo = sourceInfo;
			}
		}
	}

	interface Presenter<SourceInfo> {
		void onInit(View<SourceInfo> view);

		void onDestroy();

		void onAppear();

		void onDisappear();

		void onPlay();

		void onPause();

		void onStop();

		void onSeekToPosition(int positionInMilliseconds);

		void onRepeat();

		void onDoNotRepeat();

		void simulateError();
	}
}
