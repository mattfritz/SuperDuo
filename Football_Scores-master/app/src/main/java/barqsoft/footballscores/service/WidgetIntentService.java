package barqsoft.footballscores.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.FootballWidgetProvider;
import barqsoft.footballscores.R;

public class WidgetIntentService extends IntentService {
    private static final String[] FOOTBALL_COLUMNS = {
        DatabaseContract.scores_table.LEAGUE_COL,
        DatabaseContract.scores_table.DATE_COL,
        DatabaseContract.scores_table.TIME_COL,
        DatabaseContract.scores_table.HOME_COL,
        DatabaseContract.scores_table.AWAY_COL,
        DatabaseContract.scores_table.HOME_GOALS_COL,
        DatabaseContract.scores_table.AWAY_GOALS_COL,
        DatabaseContract.scores_table.MATCH_ID,
        DatabaseContract.scores_table.MATCH_DAY
    };

    private static final int COL_HOME = 3;
    private static final int COL_AWAY = 4;
    private static final int COL_HOME_GOALS = 6;
    private static final int COL_AWAY_GOALS = 7;
    private static final int COL_DATE = 1;
    private static final int COL_LEAGUE = 5;
    private static final int COL_MATCHDAY = 9;
    private static final int COL_ID = 8;
    private static final int COL_MATCHTIME = 2;

    private static final String LOG_TAG = WidgetIntentService.class.getSimpleName();

    public WidgetIntentService() {
        super("WidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                FootballWidgetProvider.class));

        // Query for matches on today's date
        Uri uri = DatabaseContract.scores_table.buildScoreWithDate();

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String[] queryDate = new String[1];
        queryDate[0] = dateFormatter.format(date);

        Cursor cursor = getContentResolver().query(uri, FOOTBALL_COLUMNS, null, queryDate, null);

        if (cursor == null) {
            Log.v(LOG_TAG, "Cursor is null");
            return;
        }
        if (!cursor.moveToFirst()) {
            Log.v(LOG_TAG, "Cursor has no data");
            cursor.close();
            return;
        }

        while (cursor.moveToNext()) {
            String id = String.valueOf(cursor.getInt(COL_ID));
            String sample = cursor.getString(COL_HOME);
            Log.v(LOG_TAG, "ID: " + id + " SAMPLE: " + sample);
        }
        // TODO: Get collection data here, might need an adapter

        for (int appWidgetId : appWidgetIds) {
            int layout = R.layout.football_widget;
            RemoteViews views = new RemoteViews(getPackageName(), layout);

            // TODO: Update widget views and add listeners here for each widget instance

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
