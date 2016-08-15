package com.develop.zuzik.audioplayerexample.widget;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * User: zuzik
 * Date: 8/15/16
 */

public class WidgetSettings {

	private static final String KEY = "widgetIds";

	private final SharedPreferences preferences;

	public WidgetSettings(Context context, String preferencesName) {
		this.preferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
	}

	public void updateWidgetIds(int[] ids) {
		Set<String> stringIds = new LinkedHashSet<>();
		for (Integer id : ids) {
			stringIds.add(String.valueOf(id));
		}
		this.preferences.edit().putStringSet(KEY, stringIds).apply();
	}

	public List<Integer> getWidgetIds() {
		Set<String> widgetsIdsStrings = this.preferences.getStringSet(KEY, new LinkedHashSet<>());
		List<Integer> widgetsIds = new ArrayList<>();
		for (String id : widgetsIdsStrings) {
			widgetsIds.add(Integer.parseInt(id));
		}
		return widgetsIds;
	}
}
