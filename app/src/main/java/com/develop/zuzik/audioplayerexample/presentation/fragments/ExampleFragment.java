package com.develop.zuzik.audioplayerexample.presentation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.develop.zuzik.audioplayerexample.R;
import com.develop.zuzik.audioplayerexample.application.App;
import com.develop.zuzik.audioplayerexample.entities.Song;
import com.develop.zuzik.audioplayerexample.mvp.player.PlayerPresenter;
import com.develop.zuzik.audioplayerexample.mvp.player.presenter_destroy_strategy.DoNothingPlayerPresenterDestroyStrategy;
import com.develop.zuzik.audioplayerexample.mvp.interfaces.Player;
import com.develop.zuzik.player.source.RawResourcePlayerSource;
import com.develop.zuzik.audioplayerexample.presentation.player_exception_message_provider.ExamplePlayerExceptionMessageProvider;

public class ExampleFragment extends Fragment implements Player.View<Song> {

	private TextView title;
	private Button play;
	private Button pause;
	private Button stop;
	private SeekBar seekBar;
	private TextView maxDuration;
	private TextView currentPosition;
	private SwitchCompat repeat;
	private View loading;

	private Player.Presenter<Song> presenter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_example, container, false);

		this.presenter = new PlayerPresenter<>(getModel(), new ExamplePlayerExceptionMessageProvider(), new DoNothingPlayerPresenterDestroyStrategy());
		this.presenter.setView(this);
		this.presenter.onSetSource(new RawResourcePlayerSource<>(new Song("Of monsters and men", "Crystal (long)", R.drawable.of_monsters_and_men_1), R.raw.song));

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

		this.presenter.onCreate();

		return view;
	}

	@Override
	public void onDestroyView() {
		this.presenter.onDestroy();
		super.onDestroyView();
	}

	@Override
	public void onStart() {
		super.onStart();
		this.presenter.onAppear();
	}

	@Override
	public void onStop() {
		this.presenter.onDisappear();
		this.presenter.setView(null);
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

	private Player.Model<Song> getModel() {
		return ((App) getActivity().getApplicationContext()).getModel();
	}
}
