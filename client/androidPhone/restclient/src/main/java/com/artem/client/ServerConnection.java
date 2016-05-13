package com.artem.client;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.artem.server.api.drawing.DrawingRawData;

import java.util.HashMap;
import java.util.Map;

/**
 * created by artems on 2/22/16.
 */
public class ServerConnection {

    private static ServerConnection instance;

    private HttpClient httpClient;
    private ConnectivityManager connMgr;

    public static ServerConnection getConnection(Activity activity) {
        if (instance == null) {
            instance = new ServerConnection(activity);
        }
        return instance;
    }

    private ServerConnection(Activity activity) {
        httpClient = new HttpClient();
        connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public void send(final RequestBuilder req) {
        if (isOnline()) {
            new AsyncTask<RequestBuilder, Void, HttpResponse>() {

                @Override
                protected HttpResponse doInBackground(RequestBuilder... request) {
                    return httpClient.send(request[0]);
                }

                @Override
                protected void onPostExecute(HttpResponse response) {
                    ResponseListener listener = req.getResponseListener();
                    if (listener != null) listener.onResponse(response);
                }
            }.execute(req);
        } else {
            Log.e(getClass().getSimpleName(), "No connectivity, request " + req + " not sent");
        }
    }

    public boolean isOnline() {
        Log.d(ServerConnection.class.getSimpleName(), "checking online status");
        if (httpClient == null) return false;

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
