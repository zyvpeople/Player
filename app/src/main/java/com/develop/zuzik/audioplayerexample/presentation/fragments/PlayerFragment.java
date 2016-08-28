package com.develop.zuzik.audioplayerexample.presentation.fragments;

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
import com.develop.zuzik.audioplayerexample.application.App;
import com.develop.zuzik.audioplayerexample.domain.ExampleNextControlAvailabilityStrategy;
import com.develop.zuzik.audioplayerexample.domain.ExamplePreviousControlAvailabilityStrategy;
import com.develop.zuzik.audioplayerexample.domain.Song;
import com.develop.zuzik.multipleplayer.player_source_release_strategy.DoNotReleaseIfExistsPlayerSourceReleaseStrategy;
import com.develop.zuzik.multipleplayermvp.presenter.MultiplePlayerControlPresenter;
import com.develop.zuzik.multipleplayermvp.presenter.MultiplePlayerHidingPresenter;
import com.develop.zuzik.multipleplayermvp.presenter_destroy_strategy.ClearSourcesMultiplePlayerPresenterDestroyStrategy;
import com.develop.zuzik.player.source.RawResourcePlayerSource;
import com.develop.zuzik.player.volume.Volume;
import com.develop.zuzik.audioplayerexample.presentation.adapters.SongViewPagerAdapter;
import com.develop.zuzik.audioplayerexample.presentation.player_exception_message_provider.ExamplePlayerExceptionMessageProvider;
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer;
import com.develop.zuzik.multipleplayermvp.presenter.MultiplePlayerSourcesPresenter;
import com.develop.zuzik.player.source.PlayerSource;
import com.develop.zuzik.player.source.UriPlayerSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class PlayerFragment
		extends Fragment
		implements MultiplePlayer.SourcesView<Song>, MultiplePlayer.ControlView<Song> {

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
	private ImageView shuffle;
	private ImageView playPause;

	private ImageView skipNext;
	private ImageView skipPrevious;

	private SongViewPagerAdapter adapter;

	private MultiplePlayer.SourcesPresenter<Song> presenter;
	private MultiplePlayer.ControlPresenter<Song> controlPresenter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		new Volume().useVolumeKeysToControlPlaybackVolume(getActivity());

		this.presenter = new MultiplePlayerSourcesPresenter<>(
				getModel(),
				new ClearSourcesMultiplePlayerPresenterDestroyStrategy<>(),
				new DoNotReleaseIfExistsPlayerSourceReleaseStrategy<>(),
				new ExamplePlayerExceptionMessageProvider());
		this.controlPresenter = new MultiplePlayerControlPresenter<>(
				getModel(),
				new ExampleNextControlAvailabilityStrategy(),
				new ExamplePreviousControlAvailabilityStrategy());

		this.presenter.setView(this);
		this.controlPresenter.setView(this);

		this.presenter.onSetPlayerSources(
				Arrays.asList(
						new RawResourcePlayerSource<>(new Song("Image", "Image", R.drawable.enter_shikari_1), R.raw.enter_shikari_1),
						new RawResourcePlayerSource<>(new Song("Enter Shikari", "local video", R.drawable.enter_shikari_1), R.raw.video),
						new UriPlayerSource<>(new Song("Ha ha", "remote video", R.drawable.enter_shikari_1), "http://fs144.www.ex.ua/get/98f9f422e098823c72727f20fbdcbc8b/225821344/Hardcore_tr2_oKino.ua.mp4"),
						new UriPlayerSource<>(new Song("Enter Shikari", "remote video", R.drawable.enter_shikari_1), "https://youtu.be/tLeg_5ljVcA"),
						new RawResourcePlayerSource<>(new Song("Of monsters and men", "Crystal", R.drawable.of_monsters_and_men_1), R.raw.song),
						new RawResourcePlayerSource<>(new Song("Of monsters and men", "Crystal", R.drawable.of_monsters_and_men_2), R.raw.song_short),
						new UriPlayerSource<>(new Song("Enter Shikari", "Enter Shikari", R.drawable.enter_shikari_1), "http://www.ex.ua/get/147185586"),
						new RawResourcePlayerSource<>(new Song("Enter Shikari", "Take it back", R.drawable.enter_shikari_2), R.raw.song_take_it_back)));

		this.presenter.onCreate();
		this.controlPresenter.onCreate();
	}

	@Override
	public void onDestroy() {
		this.presenter.onDestroy();
		this.controlPresenter.onDestroy();
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

		this.adapter = new SongViewPagerAdapter(getChildFragmentManager(), new ArrayList<PlayerSource<Song>>());
		this.viewPager.setAdapter(this.adapter);

		this.playPause.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TAG_STATE_PLAY.equals(v.getTag())) {
					PlayerFragment.this.controlPresenter.onPlay();
				} else if (TAG_STATE_PAUSE.equals(v.getTag())) {
					PlayerFragment.this.controlPresenter.onPause();
				} else {
					Log.w(PlayerFragment.this.getClass().getSimpleName(), "Tag is not set");
				}
			}
		});
		this.repeat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TAG_STATE_REPEAT_ON.equals(v.getTag())) {
					PlayerFragment.this.controlPresenter.onDoNotRepeat();
				} else if (TAG_STATE_REPEAT_OFF.equals(v.getTag())) {
					PlayerFragment.this.controlPresenter.onRepeat();
				} else {
					Log.w(PlayerFragment.this.getClass().getSimpleName(), "Tag is not set");
				}
			}
		});
		skipPrevious.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PlayerFragment.this.controlPresenter.onSkipPrevious();
			}
		});
		skipNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PlayerFragment.this.controlPresenter.onSkipNext();
			}
		});
		this.shuffle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TAG_STATE_SHUFFLE_ON.equals(v.getTag())) {
					PlayerFragment.this.controlPresenter.onDoNotShuffle();
				} else if (TAG_STATE_SHUFFLE_OFF.equals(v.getTag())) {
					PlayerFragment.this.controlPresenter.onShuffle();
				} else {
					Log.w(PlayerFragment.this.getClass().getSimpleName(), "Tag is not set");
				}
			}
		});
		this.progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser) {
					controlPresenter.onSeekToPosition(progress);
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
			this.playPause.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					PlayerFragment.this.presenter.simulateError();
					return true;
				}
			});
		}

		this.presenter.onAppear();
		this.controlPresenter.onAppear();

		return view;
	}

	@Override
	public void onDestroyView() {
		this.presenter.onDisappear();
		this.controlPresenter.onDisappear();
		super.onDestroyView();
	}

	//region Player.View

	@Override
	public void showError(String message) {
		Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void displayCurrentSource(PlayerSource<Song> song) {
		this.singer.setText(song.getSourceInfo().artist);
		this.song.setText(song.getSourceInfo().name);
		int songIndex = this.adapter.getSongs().indexOf(song);
		if (songIndex != -1 && this.viewPager.getCurrentItem() != songIndex) {
			this.viewPager.removeOnPageChangeListener(this.onPageChangeListener);
			this.viewPager.setCurrentItem(songIndex);
			this.viewPager.addOnPageChangeListener(this.onPageChangeListener);
		}
	}

	@Override
	public void doNotDisplayCurrentSource() {
		this.singer.setText("");
		this.song.setText("");
		this.viewPager.setCurrentItem(-1);
	}

	@Override
	public void displaySources(List<PlayerSource<Song>> playerSources) {
		if (!this.adapter.getSongs().equals(playerSources)) {
			this.viewPager.removeOnPageChangeListener(this.onPageChangeListener);
			this.adapter.setSongs(playerSources);
			this.adapter.notifyDataSetChanged();

			if (playerSources.isEmpty()) {
				this.adapter = new SongViewPagerAdapter(getChildFragmentManager(), new ArrayList<PlayerSource<Song>>());
				this.viewPager.setAdapter(this.adapter);
			}
		}
	}

	//endregion

	//region MultiplePlayer.ControlView

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
		this.currentTime.setText(timeToRepresentation(currentTimeInMilliseconds));
		this.totalTime.setText(timeToRepresentation(totalTimeInMilliseconds));
	}

	@Override
	public void showProgress() {
		this.progress.setVisibility(View.VISIBLE);
		this.currentTime.setVisibility(View.VISIBLE);
		this.totalTime.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgress() {
		this.progress.setVisibility(View.GONE);
		this.currentTime.setVisibility(View.GONE);
		this.totalTime.setVisibility(View.GONE);
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
	public void enableSwitchControls(boolean next, boolean previous) {
		this.skipNext.setVisibility(next ? View.VISIBLE : View.INVISIBLE);
		this.skipPrevious.setVisibility(previous ? View.VISIBLE : View.INVISIBLE);
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

	private MultiplePlayer.Model<Song> getModel() {
		return ((App) getActivity().getApplicationContext()).getMultiplePlayerModel();
	}

	private String timeToRepresentation(long milliseconds) {
		return String.format(Locale.getDefault(),
				"%d:%02d",
				TimeUnit.MILLISECONDS.toMinutes(milliseconds),
				TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
						TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
	}

	private final ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

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
	};
}
