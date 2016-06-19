package com.develop.zuzik.audioplayerexample.entities;

import android.support.annotation.DrawableRes;

import java.io.Serializable;

/**
 * User: zuzik
 * Date: 6/19/16
 */
public class Song implements Serializable {
	public final String artist;
	public final String name;
	@DrawableRes
	public final int image;

	public Song(String artist, String name, @DrawableRes int image) {
		this.artist = artist;
		this.image = image;
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o instanceof Song) {
			Song song = (Song) o;
			return this.artist.equals(song.artist)
					&& this.name.equals(song.name)
					&& this.image == song.image;
		} else {
			return false;
		}
	}
}
