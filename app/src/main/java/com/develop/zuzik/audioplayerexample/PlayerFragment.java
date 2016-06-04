package com.develop.zuzik.audioplayerexample;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.develop.zuzik.audioplayerexample.mvp.implementations.models.PlayerModel;
import com.develop.zuzik.audioplayerexample.mvp.implementations.presenters.PlayerPresenter;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.Player;
import com.develop.zuzik.audioplayerexample.player.PlaybackBundle;
import com.develop.zuzik.audioplayerexample.player.PlaybackState;

import java.util.concurrent.TimeUnit;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class PlayerFragment extends Fragment implements Player.View {

	private static final String TAG_STATE_PLAY = "TAG_STATE_PLAY";
	private static final String TAG_STATE_PAUSE = "TAG_STATE_PAUSE";

	private static final String TAG_STATE_REPEAT_ON = "TAG_STATE_REPEAT_ON";
	private static final String TAG_STATE_REPEAT_OFF = "TAG_STATE_REPEAT_OFF";

	private TextView currentTime;
	private TextView totalTime;

	private SeekBar progress;

	private TextView singer;
	private TextView song;

	private ImageView repeat;
	private ImageView skipPrevious;
	private ImageView skipNext;
	private ImageView shuffle;
	private ImageView playPause;

	private Player.Presenter presenter = new PlayerPresenter(new PlayerModel(R.raw.song));

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.presenter.onInit(this);
	}

	@Override
	public void onDestroy() {
		this.presenter.onDestroy();
		super.onDestroy();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_player, container, false);

		this.currentTime = (TextView) view.findViewById(R.id.currentTime);
		this.totalTime = (TextView) view.findViewById(R.id.totalTime);
		this.progress = (SeekBar) view.findViewById(R.id.progress);
		this.singer = (TextView) view.findViewById(R.id.singer);
		this.song = (TextView) view.findViewById(R.id.song);
		this.repeat = (ImageView) view.findViewById(R.id.repeat);
		this.skipPrevious = (ImageView) view.findViewById(R.id.skipPrevious);
		this.skipNext = (ImageView) view.findViewById(R.id.skipNext);
		this.shuffle = (ImageView) view.findViewById(R.id.shuffle);
		this.playPause = (ImageView) view.findViewById(R.id.playPause);

		this.singer.setText("Of monsters and men");
		this.song.setText("Little talks");

		this.playPause.setOnClickListener(v -> {
			if (TAG_STATE_PLAY.equals(v.getTag())) {
				this.presenter.onPlay(getActivity());
			} else if (TAG_STATE_PAUSE.equals(v.getTag())) {
				this.presenter.onPause();
			} else {
				Log.w(getClass().getSimpleName(), "Tag is not set");
			}
		});
		this.repeat.setOnClickListener(v -> {
			if (TAG_STATE_REPEAT_ON.equals(v.getTag())) {
				this.presenter.onDoNotRepeat();
			} else if (TAG_STATE_REPEAT_OFF.equals(v.getTag())) {
				this.presenter.onRepeat();
			} else {
				Log.w(getClass().getSimpleName(), "Tag is not set");
			}
		});
		this.progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

		this.presenter.onAppear();

		return view;
	}

	@Override
	public void onDestroyView() {
		this.presenter.onDisappear();
		super.onDestroyView();
	}

	//region Player.View

	@Override
	public void display(PlaybackBundle bundle, boolean repeat) {
		if (bundle.state == PlaybackState.ERROR) {
			Toast.makeText(getActivity(), "Error playing song", Toast.LENGTH_SHORT).show();
		}

		this.totalTime.setText(bundle.maxDurationInMilliseconds != null
				? timeToRepresentation(bundle.maxDurationInMilliseconds)
				: "");
		this.currentTime.setText(bundle.maxDurationInMilliseconds != null
				? timeToRepresentation(bundle.currentPositionInMilliseconds)
				: "");
		this.progress.setVisibility(bundle.maxDurationInMilliseconds != null
				? View.VISIBLE
				: View.GONE);
		this.progress.setMax(bundle.maxDurationInMilliseconds != null
				? bundle.maxDurationInMilliseconds
				: 100);
		this.progress.setProgress(bundle.maxDurationInMilliseconds != null
				? bundle.currentPositionInMilliseconds
				: 0);

		if (bundle.state == PlaybackState.PLAYING) {
			showPlayPauseButtonAsPause();
		} else {
			showPlayPauseButtonAsPlay();
		}

		if (repeat) {
			showRepeatButtonAsOn();
		} else {
			showRepeatButtonAsOff();
		}
	}

	//endregion

	private String timeToRepresentation(long milliseconds) {
		return String.format("%d:%02d",
				TimeUnit.MILLISECONDS.toMinutes(milliseconds),
				TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
						TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
	}

	private void showPlayPauseButtonAsPlay() {
		applyStateToPlayPauseButton(TAG_STATE_PLAY, R.drawable.ic_play);
	}

	private void showPlayPauseButtonAsPause() {
		applyStateToPlayPauseButton(TAG_STATE_PAUSE, R.drawable.ic_pause);
	}

	private void applyStateToPlayPauseButton(String tag, @DrawableRes int imageResId) {
		this.playPause.setTag(tag);
		this.playPause.setImageResource(imageResId);
	}

	private void showRepeatButtonAsOn() {
		applyStateToRepeatButton(TAG_STATE_REPEAT_ON, R.drawable.ic_repeat_on);
	}

	private void showRepeatButtonAsOff() {
		applyStateToRepeatButton(TAG_STATE_REPEAT_OFF, R.drawable.ic_repeat_off);
	}

	private void applyStateToRepeatButton(String tag, @DrawableRes int imageResId) {
		this.repeat.setTag(tag);
		this.repeat.setImageResource(imageResId);
	}
}
