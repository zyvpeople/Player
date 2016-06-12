package com.develop.zuzik.audioplayerexample.presentation.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.develop.zuzik.audioplayerexample.BuildConfig;
import com.develop.zuzik.audioplayerexample.R;
import com.develop.zuzik.audioplayerexample.mvp.implementations.models.MultiplePlayerModel;
import com.develop.zuzik.audioplayerexample.mvp.implementations.presenters.MultiplePlayerPresenter;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.MultiplePlayer;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.MultiplePlayback;
import com.develop.zuzik.audioplayerexample.player.player_initializer.RawResourcePlayerInitializer;
import com.develop.zuzik.audioplayerexample.player.player_initializer.UriPlayerInitializer;
import com.develop.zuzik.audioplayerexample.presentation.adapters.SongDetailViewPagerAdapter;
import com.develop.zuzik.audioplayerexample.presentation.player_exception_message_provider.ExamplePlayerExceptionMessageProvider;

import java.util.Arrays;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class PlayerFragment extends Fragment implements MultiplePlayer.View {

	private static final String TAG_STATE_PLAY = "TAG_STATE_PLAY";
	private static final String TAG_STATE_PAUSE = "TAG_STATE_PAUSE";

	private static final String TAG_STATE_REPEAT_ON = "TAG_STATE_REPEAT_ON";
	private static final String TAG_STATE_REPEAT_OFF = "TAG_STATE_REPEAT_OFF";

	private ViewPager viewPager;

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

	private SongDetailViewPagerAdapter adapter;

	private MultiplePlayer.Presenter presenter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.presenter = new MultiplePlayerPresenter(
				new MultiplePlayerModel(
						new MultiplePlayback(
								getContext(),
								Arrays.asList(
										new RawResourcePlayerInitializer(R.raw.song),
										new RawResourcePlayerInitializer(R.raw.song_short),
										new UriPlayerInitializer(Uri.parse("http://picosong.com/cdn/8768acb97f1c9333b01b1c545756ff81.mp3"))))),
				new ExamplePlayerExceptionMessageProvider());
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

		this.viewPager = (ViewPager) view.findViewById(R.id.viewPager);

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

		this.adapter = new SongDetailViewPagerAdapter(getChildFragmentManager(), 3);
		this.viewPager.setAdapter(this.adapter);
		this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

		this.playPause.setOnClickListener(v -> {
			if (TAG_STATE_PLAY.equals(v.getTag())) {
				this.presenter.onPlay();
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
		this.skipPrevious.setOnClickListener(v -> this.presenter.onSkipPrevious());
		this.skipNext.setOnClickListener(v -> this.presenter.onSkipNext());
		this.shuffle.setOnClickListener(v -> Toast.makeText(getContext(), "Not implemented yet", Toast.LENGTH_SHORT).show());
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

		if (BuildConfig.DEBUG == true) {
			this.playPause.setOnLongClickListener(v -> {
				this.presenter.simulateError();
				return true;
			});
		}

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
	public void repeat() {
		showRepeatButtonAsOn();
	}

	@Override
	public void doNotRepeat() {
		showRepeatButtonAsOff();
	}

	@Override
	public void setProgress(int currentTimeInMilliseconds, int totalTimeInMilliseconds) {
		this.progress.setProgress(currentTimeInMilliseconds);
		this.progress.setMax(totalTimeInMilliseconds);
	}

	@Override
	public void showProgress() {
		this.progress.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgress() {
		this.progress.setVisibility(View.GONE);
	}

	@Override
	public void showTime(String currentTime, String totalTime) {
		this.currentTime.setText(currentTime);
		this.totalTime.setText(totalTime);
	}

	@Override
	public void showError(String message) {
		Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void enablePlayControls(boolean play, boolean pause, boolean stop) {
		if (play) {
			showPlayPauseButtonAsPlay();
		} else if (pause) {
			showPlayPauseButtonAsPause();
		} else {
		}
	}

	//endregion

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
