package com.develop.zuzik.audioplayerexample.presentation.appwidget_provider;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

import com.develop.zuzik.audioplayerexample.application.App;
import com.develop.zuzik.audioplayerexample.domain.Song;
import com.develop.zuzik.audioplayerexample.presentation.remove_views.PlayerRemoteView;
import com.develop.zuzik.audioplayerexample.widget.WidgetSettings;
import com.develop.zuzik.playermvp.interfaces.Player;

/**
 * User: zuzik
 * Date: 8/14/16
 */

public class PlayerAppWidgetProvider extends AppWidgetProvider {

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
//		Log.i(getClass().getSimpleName(), "onEnabled");
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		getSettings(context).updateWidgetIds(appWidgetIds);
		for (Integer id : appWidgetIds) {
			update(context, appWidgetManager, id);
		}
//		Player.Model<Song> model = ((App) context.getApplicationContext()).getModel();
//		Log.i(getClass().getSimpleName(), model.getState().transform(input -> input.playerSource.getSourceInfo().name).or("empty"));
//		Log.i(getClass().getSimpleName(), "onUpdate");
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
//		Log.i(getClass().getSimpleName(), "onDeleted");
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
//		Log.i(getClass().getSimpleName(), "onDisabled");
		super.onDisabled(context);
	}

	private static WidgetSettings getSettings(Context context) {
		return ((App) context.getApplicationContext()).getWidgetSettings();
	}

	private static Player.Model<Song> getModel(Context context) {
		return ((App) context.getApplicationContext()).getModel();
	}

	public static void update(Context context, AppWidgetManager manager, int widgetId) {
		Player.Model<Song> model = getModel(context);

		PlayerRemoteView view = new PlayerRemoteView(context);
		if (model.getState().isPresent()) {
			view.setSong(model.getState().get().playerSource.getSourceInfo());
		} else {
			//TODO: clear view
		}

		manager.updateAppWidget(widgetId, view);
	}
}
