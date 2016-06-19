package com.develop.zuzik.audioplayerexample.entities;

import java.io.Serializable;

/**
 * User: zuzik
 * Date: 6/19/16
 */
public class Song implements Serializable{
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
