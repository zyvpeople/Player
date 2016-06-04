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

import com.develop.zuzik.audioplayerexample.R;
import com.develop.zuzik.audioplayerexample.mvp.implementations.models.MultiplePlayerModel;
import com.develop.zuzik.audioplayerexample.mvp.implementations.presenters.MultiplePlayerPresenter;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.MultiplePlayer;
import com.develop.zuzik.audioplayerexample.player.MultiplePlayback;
import com.develop.zuzik.audioplayerexample.player.MultiplePlaybackBundle;
import com.develop.zuzik.audioplayerexample.player.MultiplePlaybackRepeatMode;
import com.develop.zuzik.audioplayerexample.player.PlaybackBundle;
import com.develop.zuzik.audioplayerexample.player.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.player_source.RawResourcePlayerSource;
import com.develop.zuzik.audioplayerexample.player.player_source.UriPlayerSource;
import com.develop.zuzik.audioplayerexample.presentation.adapters.SongDetailViewPagerAdapter;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

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

	private MultiplePlayer.Presenter presenter = new MultiplePlayerPresenter(
			new MultiplePlayerModel(
					new MultiplePlayback(
							Arrays.asList(
									new RawResourcePlayerSource(R.raw.song),
									new RawResourcePlayerSource(R.raw.song_short),
									new UriPlayerSource(Uri.parse("http://storage.mp3.cc/download/454079/dG5Dd2NNMy8vZ1NUc2hINFZtRXl4OUt4c2RjZXhvdmkra3liTmFnOTFWMlZibUlCMlZRTXcwcVVhckszaldDSGRqMzRLaTg2ckpkQVhxZHYya3NKc09MM0VvNnFFQ2g3ZnNUYTlMS3M2YlY5MkhtcEpYTlR4V1JPaUJUcHhWMU8/Of_Monsters_And_Men-Little_Talks_(mp3.cc).mp3"))))));

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.presenter.onInit(getContext(), this);
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
				this.presenter.onPlay(getContext());
			} else if (TAG_STATE_PAUSE.equals(v.getTag())) {
				this.presenter.onPause();
			} else {
				Log.w(getClass().getSimpleName(), "Tag is not set");
			}
		});
		this.repeat.setOnClickListener(v -> {
			Toast.makeText(getContext(), "Not implemented yet", Toast.LENGTH_SHORT).show();
			//TODO: uncomment and test for multiple playback
//			if (TAG_STATE_REPEAT_ON.equals(v.getTag())) {
//				this.presenter.onRepeat(MultiplePlaybackRepeatMode.DO_NOT_REPEAT);
//			} else if (TAG_STATE_REPEAT_OFF.equals(v.getTag())) {
//				this.presenter.onRepeat(MultiplePlaybackRepeatMode.REPEAT_ONE);
//			} else {
//				Log.w(getClass().getSimpleName(), "Tag is not set");
//			}
		});
		this.skipPrevious.setOnClickListener(v -> this.presenter.onSkipPrevious(getContext()));
		this.skipNext.setOnClickListener(v -> this.presenter.onSkipNext(getContext()));
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
	public void display(MultiplePlaybackBundle bundle, MultiplePlaybackRepeatMode repeatMode) {
		PlaybackBundle playbackBundle = bundle.currentPlaybackBundle;
		PlaybackState playbackState = playbackBundle != null ? playbackBundle.state : PlaybackState.NONE;
		Integer maxDurationInMilliseconds = playbackBundle != null ? playbackBundle.maxDurationInMilliseconds : null;
		int currentPositionInMilliseconds = playbackBundle != null ? playbackBundle.currentPositionInMilliseconds : 0;

		if (playbackState == PlaybackState.ERROR) {
			Toast.makeText(getContext(), "Error playing song", Toast.LENGTH_SHORT).show();
		}

		this.totalTime.setText(maxDurationInMilliseconds != null
				? timeToRepresentation(maxDurationInMilliseconds)
				: "");
		this.currentTime.setText(maxDurationInMilliseconds != null
				? timeToRepresentation(currentPositionInMilliseconds)
				: "");
		this.progress.setVisibility(maxDurationInMilliseconds != null
				? View.VISIBLE
				: View.GONE);
		this.progress.setMax(maxDurationInMilliseconds != null
				? maxDurationInMilliseconds
				: 100);
		this.progress.setProgress(maxDurationInMilliseconds != null
				? currentPositionInMilliseconds
				: 0);

		if (playbackState == PlaybackState.PLAYING) {
			showPlayPauseButtonAsPause();
		} else {
			showPlayPauseButtonAsPlay();
		}

		switch (repeatMode) {
			case REPEAT_ONE:
				showRepeatButtonAsOn();
				break;
			case REPEAT_ALL:
				//TODO: handle
				break;
			case DO_NOT_REPEAT:
				showRepeatButtonAsOff();
				break;
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
