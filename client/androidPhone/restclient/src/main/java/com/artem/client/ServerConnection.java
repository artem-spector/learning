package com.artem.client;

import android.util.Log;

import com.artem.client.drawing.DrawingRawData;

/**
 * created by artems on 2/22/16.
 */
public class ServerConnection {

    private String serverURL;

    public ServerConnection(String serverURL) {
        this.serverURL = serverURL;
    }

    public void sendDrawing(DrawingRawData data) {
        Log.d("ServerConnection", "sending drawing tp " + serverURL);
    }
}
