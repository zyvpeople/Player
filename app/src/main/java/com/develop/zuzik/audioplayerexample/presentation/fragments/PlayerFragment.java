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
import com.develop.zuzik.audioplayerexample.entities.Song;
import com.develop.zuzik.audioplayerexample.mvp.implementations.models.MultiplePlayerModel;
import com.develop.zuzik.audioplayerexample.mvp.implementations.presenters.MultiplePlayerPresenter;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.MultiplePlayer;
import com.develop.zuzik.audioplayerexample.player.exceptions.AudioServiceNotSupportException;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.factories.ExampleNextPlayerSourceStrategyFactory;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.factories.ExamplePreviousPlayerSourceStrategyFactory;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.develop.zuzik.audioplayerexample.player.player_source.RawResourcePlayerSource;
import com.develop.zuzik.audioplayerexample.player.player_source.UriPlayerSource;
import com.develop.zuzik.audioplayerexample.presentation.adapters.SongViewPagerAdapter;
import com.develop.zuzik.audioplayerexample.presentation.player_exception_message_provider.ExamplePlayerExceptionMessageProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class PlayerFragment extends Fragment implements MultiplePlayer.View<Song> {

	private static final String TAG_STATE_PLAY = "TAG_STATE_PLAY";
	private static final String TAG_STATE_PAUSE = "TAG_STATE_PAUSE";

	private static final String TAG_STATE_REPEAT_ON = "TAG_STATE_REPEAT_ON";
	private static final String TAG_STATE_REPEAT_OFF = "TAG_STATE_REPEAT_OFF";

	private static final String TAG_STATE_SHUFFLE_ON = "TAG_STATE_SHUFFLE_ON";
	private static final String TAG_STATE_SHUFFLE_OFF = "TAG_STATE_SHUFFLE_OFF";

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

	private SongViewPagerAdapter adapter;

	private MultiplePlayer.Presenter<Song> presenter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			this.presenter = new MultiplePlayerPresenter<Song>(
					new MultiplePlayerModel<>(
							getContext(),
							Arrays.asList(
									new RawResourcePlayerSource<>(new Song("Crystal (long)"), R.raw.song),
									new RawResourcePlayerSource<>(new Song("Crystal (short)"), R.raw.song_short),
									new UriPlayerSource<>(new Song("Enter shikary (network)"), Uri.parse("http://picosong.com/cdn/8768acb97f1c9333b01b1c545756ff81.mp3")),
									new RawResourcePlayerSource<>(new Song("Take it back"), R.raw.song_take_it_back)),
							new ExampleNextPlayerSourceStrategyFactory<>(),
							new ExamplePreviousPlayerSourceStrategyFactory<>()),
					new ExamplePlayerExceptionMessageProvider());
		} catch (AudioServiceNotSupportException e) {
			throw new RuntimeException(e);
		}
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

		this.adapter = new SongViewPagerAdapter(getChildFragmentManager(), new ArrayList<>());
		this.viewPager.setAdapter(this.adapter);
		this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
				presenter.onSwitchToSource(adapter.getSongs().get(position));
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
				this.presenter.onDoNotRepeatSingle();
			} else if (TAG_STATE_REPEAT_OFF.equals(v.getTag())) {
				this.presenter.onRepeatSingle();
			} else {
				Log.w(getClass().getSimpleName(), "Tag is not set");
			}
		});
		this.skipPrevious.setOnClickListener(v -> this.presenter.onSkipPrevious());
		this.skipNext.setOnClickListener(v -> this.presenter.onSkipNext());
		this.shuffle.setOnClickListener(v -> {
			if (TAG_STATE_SHUFFLE_ON.equals(v.getTag())) {
				this.presenter.onDoNotShuffle();
			} else if (TAG_STATE_SHUFFLE_OFF.equals(v.getTag())) {
				this.presenter.onShuffle();
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

		if (BuildConfig.DEBUG) {
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
	public void shuffle() {
		showShuffleButtonAsOn();
	}

	@Override
	public void doNotShuffle() {
		showShuffleButtonAsOff();
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

	@Override
	public void displayCurrentSource(PlayerSource<Song> song) {
		this.singer.setText("Not set yet");
		this.song.setText(song.getSourceInfo().name);
		int songIndex = this.adapter.getSongs().indexOf(song);
		if (songIndex != -1 && this.viewPager.getCurrentItem() != songIndex) {
			this.viewPager.setCurrentItem(songIndex);
		}
	}

	@Override
	public void doNotDisplayCurrentSource() {
		this.singer.setText("");
		this.song.setText("");
	}

	@Override
	public void displaySources(List<PlayerSource<Song>> playerSources) {
		if (!this.adapter.getSongs().equals(playerSources)) {
			this.adapter.setSongs(playerSources);
			this.adapter.notifyDataSetChanged();
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

	private void showShuffleButtonAsOn() {
		applyStateToShuffleButton(TAG_STATE_SHUFFLE_ON, R.drawable.ic_shuffle_on);
	}

	private void showShuffleButtonAsOff() {
		applyStateToShuffleButton(TAG_STATE_SHUFFLE_OFF, R.drawable.ic_shuffle_off);
	}

	private void applyStateToShuffleButton(String tag, @DrawableRes int imageResId) {
		this.shuffle.setTag(tag);
		this.shuffle.setImageResource(imageResId);
	}
}
