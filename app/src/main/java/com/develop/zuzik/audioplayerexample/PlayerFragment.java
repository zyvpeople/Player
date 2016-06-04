package com.develop.zuzik.audioplayerexample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class PlayerFragment extends Fragment {

	private TextView currentTime;
	private TextView totalTime;

	private SeekBar progress;

	private TextView singer;
	private TextView song;

	private View repeat;
	private View skipPrevious;
	private View skipNext;
	private View shuffle;
	private ImageView playPause;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_player, container, false);

		this.currentTime = (TextView) view.findViewById(R.id.currentTime);
		this.totalTime = (TextView) view.findViewById(R.id.totalTime);
		this.progress = (SeekBar) view.findViewById(R.id.progress);
		this.singer = (TextView) view.findViewById(R.id.singer);
		this.song = (TextView) view.findViewById(R.id.song);
		this.repeat = view.findViewById(R.id.repeat);
		this.skipPrevious = view.findViewById(R.id.skipPrevious);
		this.skipNext = view.findViewById(R.id.skipNext);
		this.shuffle = view.findViewById(R.id.shuffle);
		this.playPause = (ImageView) view.findViewById(R.id.playPause);

		this.currentTime.setText("04:32");
		this.totalTime.setText("10:53");
		this.progress.setMax(100);
		this.progress.setProgress(35);
		this.singer.setText("Of monsters and men");
		this.song.setText("Little talks");

		this.playPause.setImageResource(R.drawable.ic_play);

		return view;
	}
}
