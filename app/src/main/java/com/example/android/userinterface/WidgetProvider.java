package com.example.android.userinterface;

import android.app.PendingIntent;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class WidgetProvider extends AppWidgetProvider {
    private static final String TAG = "WidgetProvider";

    @Override
    public void onUpdate(Context context,
                         AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        Log.i(TAG, "onUpdate");

        for (int widgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(
                    context.getPackageName(), R.layout.widget_layout);

            Intent intent = new Intent(context, WidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
            );

            remoteViews.setOnClickPendingIntent(R.id.action_button, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
            sendRequest(context);

        }

    }

    public void sendRequest(Context c){

// Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(c);
        String url ="http://192.168.0.160:303/";
        String backupUrl = "http://71.232.25.252:303/";

// Request a string response from the provided URL.

        final StringRequest backupRequest = new StringRequest(Request.Method.GET, backupUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        int max = response.length() > 500 ? 500 : response.length();
                        Log.i(TAG, "Response is: "+ response.substring(0,max));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error: "+error.getMessage());
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        int max = response.length() > 500 ? 500 : response.length();
                        Log.i(TAG, "Response is: "+ response.substring(0,max));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error: "+error.getMessage());
                queue.add(backupRequest);
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
