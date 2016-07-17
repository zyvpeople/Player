package com.develop.zuzik.audioplayerexample.presentation.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.develop.zuzik.audioplayerexample.domain.Song;
import com.develop.zuzik.player.source.PlayerSource;
import com.develop.zuzik.audioplayerexample.presentation.fragments.SongFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class SongViewPagerAdapter extends FragmentStatePagerAdapter {

	private List<PlayerSource<Song>> songs = new ArrayList<>();

	public SongViewPagerAdapter(FragmentManager fm, List<PlayerSource<Song>> songs) {
		super(fm);
		this.songs = songs;
	}

	public List<PlayerSource<Song>> getSongs() {
		return songs;
	}

	public void setSongs(List<PlayerSource<Song>> songs) {
		this.songs = songs;
	}

	@Override
	public Fragment getItem(int position) {
		return SongFragment.newInstance(this.songs.get(position).getSourceInfo());
	}

	@Override
	public int getCount() {
		return this.songs.size();
	}
}
