package com.develop.zuzik.audioplayerexample.presentation.appwidget_provider;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

import com.develop.zuzik.audioplayerexample.application.App;
import com.develop.zuzik.audioplayerexample.domain.Song;
import com.develop.zuzik.audioplayerexample.presentation.remove_views.PlayerRemoteView;
import com.develop.zuzik.playermvp.interfaces.Player;

/**
 * User: zuzik
 * Date: 8/14/16
 */

public class PlayerAppWidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		for (Integer id : appWidgetIds) {
			update(context, appWidgetManager, id);
		}
	}

	private static Player.Model<Song> getModel(Context context) {
		return ((App) context.getApplicationContext()).getModel();
	}

	public static void update(Context context, AppWidgetManager manager, int widgetId) {
		Player.Model<Song> model = getModel(context);

		PlayerRemoteView view = new PlayerRemoteView(context);
		if (model.getState().isPresent()) {
			view.setSong(context, model.getState().get().playerSource.getSourceInfo());
		} else {
			//TODO: clear view
		}

		manager.updateAppWidget(widgetId, view);
	}
}
