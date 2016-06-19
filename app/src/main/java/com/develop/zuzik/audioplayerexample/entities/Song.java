package com.develop.zuzik.audioplayerexample.entities;

/**
 * User: zuzik
 * Date: 6/19/16
 */
public class Song {
	public final String name;

	public Song(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o instanceof Song) {
			return this.name.equals(((Song) o).name);
		} else {
			return false;
		}
	}
}
