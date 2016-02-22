package com.artem.client;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * created by artems on 2/22/16.
 */
public class HttpClient {

    private static final String SERVER_IP = "192.168.43.193";
    private String serverUrl = "http://" + SERVER_IP + ":8080";

    public HttpResponse post(String path, Map<String, String> params) {
        Log.d(getClass().getSimpleName(), "sending request to " + path);
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(serverUrl + path).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
            String str = "";
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (!str.isEmpty()) str += "&";
                str += entry.getKey() + "=" + entry.getValue();
            }
            out.write(str.getBytes());
            out.flush();

            int code = conn.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String body = "";
            String line;
            while ((line = br.readLine()) != null) body += line;

            return new HttpResponse(code, body, conn.getHeaderFields());
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "Failed to send POST request", e);
            return null;
        }

    }
}
