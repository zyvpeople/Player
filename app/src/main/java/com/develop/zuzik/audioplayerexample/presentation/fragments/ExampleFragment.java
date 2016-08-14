package com.develop.zuzik.audioplayerexample.presentation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.develop.zuzik.audioplayerexample.R;
import com.develop.zuzik.audioplayerexample.application.App;
import com.develop.zuzik.audioplayerexample.domain.Song;
import com.develop.zuzik.audioplayerexample.presentation.activities.VideoActivity;
import com.develop.zuzik.audioplayerexample.presentation.player_exception_message_provider.ExamplePlayerExceptionMessageProvider;
import com.develop.zuzik.player.interfaces.VideoViewSetter;
import com.develop.zuzik.player.source.RawResourcePlayerSource;
import com.develop.zuzik.player.video.Listener;
import com.develop.zuzik.player.video.SurfaceViewWrapper;
import com.develop.zuzik.playermvp.interfaces.Player;
import com.develop.zuzik.playermvp.presenter.PlayerPresenter;
import com.develop.zuzik.playermvp.presenter.PlayerVideoPresenter;
import com.develop.zuzik.playermvp.presenter_destroy_strategy.DoNothingPlayerPresenterDestroyStrategy;

public class ExampleFragment extends Fragment implements Player.View<Song>, Player.VideoView<Song> {

	private TextView title;
	private Button play;
	private Button pause;
	private Button stop;
	private SeekBar seekBar;
	private TextView maxDuration;
	private TextView currentPosition;
	private SwitchCompat repeat;
	private View loading;
	private SurfaceView surfaceView;
	private SurfaceViewWrapper surfaceViewWrapper;

	private Player.Presenter<Song> presenter;
	private Player.VideoPresenter<Song> videoPresenter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_example, container, false);

		Song song = new Song("Of monsters and men", "Crystal (long)", R.drawable.of_monsters_and_men_1);

		this.presenter = new PlayerPresenter<>(getModel(), new ExamplePlayerExceptionMessageProvider(), new DoNothingPlayerPresenterDestroyStrategy());
		this.presenter.setView(this);
//		this.presenter.onSetSource(new RawResourcePlayerSource<>(song, R.raw.song));
		this.presenter.onSetSource(new RawResourcePlayerSource<>(song, R.raw.video));

		this.videoPresenter = new PlayerVideoPresenter<>(getModel(), song);
		this.videoPresenter.setView(this);

		this.title = (TextView) view.findViewById(R.id.title);
		this.play = (Button) view.findViewById(R.id.play);
		this.pause = (Button) view.findViewById(R.id.pause);
		this.stop = (Button) view.findViewById(R.id.stop);
		Button fakeError = (Button) view.findViewById(R.id.fakeError);
		this.seekBar = (SeekBar) view.findViewById(R.id.seekBar);
		this.maxDuration = (TextView) view.findViewById(R.id.maxDuration);
		this.currentPosition = (TextView) view.findViewById(R.id.currentPosition);
		this.repeat = (SwitchCompat) view.findViewById(R.id.repeat);
		this.loading = view.findViewById(R.id.loading);
		this.surfaceView = (SurfaceView) view.findViewById(R.id.surfaceView);
		this.surfaceViewWrapper = new SurfaceViewWrapper(this.surfaceView, new Listener() {
			@Override
			public void onCreated() {
				videoPresenter.onVideoViewCreated();
			}

			@Override
			public void onDestroyed() {
				videoPresenter.onVideoViewDestroyed();
			}
		});

		this.play.setOnClickListener(v -> this.presenter.onPlay());
		this.pause.setOnClickListener(v -> this.presenter.onPause());
		this.stop.setOnClickListener(v -> this.presenter.onStop());
		fakeError.setOnClickListener(v -> this.presenter.simulateError());
		this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser) {
					presenter.onSeekToPosition(progress);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});
		this.repeat.setOnCheckedChangeListener((buttonView, isChecked) -> {
			if (isChecked) {
				this.presenter.onRepeat();
			} else {
				this.presenter.onDoNotRepeat();
			}
		});
		this.surfaceView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(VideoActivity.createIntent(getContext(), song));
			}
		});

		this.presenter.onCreate();
		this.videoPresenter.onCreate();

		return view;
	}

	@Override
	public void onDestroyView() {
		this.presenter.onDestroy();
		this.videoPresenter.onDestroy();
		super.onDestroyView();
	}

	@Override
	public void onStart() {
		super.onStart();
		this.presenter.onAppear();
		this.videoPresenter.onAppear();
	}

	@Override
	public void onStop() {
		this.presenter.onDisappear();
		this.videoPresenter.onDisappear();
		super.onStop();
	}

	//region Player.View

	@Override
	public void setRepeat() {
		this.repeat.setChecked(true);
	}

	@Override
	public void setDoNotRepeat() {
		this.repeat.setChecked(false);
	}

	@Override
	public void setProgress(int totalTimeInMilliseconds, int currentTimeInMilliseconds) {
		this.seekBar.setMax(totalTimeInMilliseconds);
		this.seekBar.setProgress(currentTimeInMilliseconds);
	}

	@Override
	public void showSourceProgress() {
		this.seekBar.setEnabled(true);
	}

	@Override
	public void hideSourceProgress() {
		this.seekBar.setEnabled(false);
	}

	@Override
	public void showTime(String currentTime, String totalTime) {
		this.currentPosition.setText(currentTime);
		this.maxDuration.setText(totalTime);
	}

	@Override
	public void showError(String message) {
		Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void enablePlayControls(boolean play, boolean pause, boolean stop) {
		this.play.setEnabled(play);
		this.pause.setEnabled(pause);
		this.stop.setEnabled(stop);
	}

	@Override
	public void showLoadingIndicator() {
		this.loading.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideLoadingIndicator() {
		this.loading.setVisibility(View.GONE);
	}

	@Override
	public void displayCurrentSource(Song song) {
		this.title.setText(song.name);
	}

	@Override
	public void doNotDisplayCurrentSource() {
		this.title.setText("");
	}

	//endregion

	//region Player.VideoView<Song>

	@Override
	public void setVideoViewAvailable() {
		this.surfaceView.setVisibility(View.VISIBLE);
	}

	@Override
	public void setVideoViewUnavailable() {
		this.surfaceView.setVisibility(View.GONE);
	}

	@Override
	public void setVideoView(VideoViewSetter setter) {
		this.surfaceViewWrapper.setVideoView(setter);
	}

	@Override
	public void clearVideoView(VideoViewSetter setter) {
		this.surfaceViewWrapper.clearVideoView(setter);
	}

	//endregion

	private Player.Model<Song> getModel() {
		return ((App) getActivity().getApplicationContext()).getModel();
	}
}
