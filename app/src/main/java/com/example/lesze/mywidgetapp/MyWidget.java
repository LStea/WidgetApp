package com.example.lesze.mywidgetapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.RemoteViews;

/**
 * Created by lesze on 10.02.2018.
 */

public class MyWidget extends AppWidgetProvider {

    private static boolean isDefaultImageVisible;

    private static MediaPlayer mediaPlayer;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget);
        views.setOnClickPendingIntent(R.id.www, createWebIntent(context));
        views.setOnClickPendingIntent(R.id.image, createPendingIntent(context, "changeImage"));
        views.setOnClickPendingIntent(R.id.play, createPendingIntent(context, "startMusic"));
        views.setOnClickPendingIntent(R.id.stop, createPendingIntent(context, "stopMusic"));
        views.setImageViewResource(R.id.image_view, R.drawable.index);
        isDefaultImageVisible = true;
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("changeImage")){
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(context, MyWidget.class));
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget);

            if (isDefaultImageVisible){
                views.setImageViewResource(R.id.image_view, R.drawable.index2);
            }
            else {
                views.setImageViewResource(R.id.image_view, R.drawable.index3);
            }

            isDefaultImageVisible = false;
            manager.updateAppWidget(appWidgetIds, views);
        }
        else if(intent.getAction().equals("startMusic")){
            mediaPlayer = MediaPlayer.create(context, R.raw.jazz);
            mediaPlayer.start();
        }
        else if(intent.getAction().equals("stopMusic")){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }

        super.onReceive(context, intent);
    }

    public static PendingIntent createWebIntent(Context context){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("https://google.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        return pendingIntent;
    }

    public static PendingIntent createPendingIntent(Context context, String action){
        Intent intent = new Intent(context, MyWidget.class);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        return pendingIntent;
    }
}

