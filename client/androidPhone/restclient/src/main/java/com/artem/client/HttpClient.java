package com.artem.client;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

//    private static final String SERVER_IP = "192.168.43.193";
    private static final String SERVER_IP = "10.100.102.10";
    private String serverUrl = "http://" + SERVER_IP + ":8080";

    private ObjectMapper mapper = new ObjectMapper();

    public HttpResponse post(String path, Map<String, Object> params) {
        Log.d(getClass().getSimpleName(), "sending request to " + path);
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(serverUrl + path).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
            String str = getParamString(params);
            out.write(str.getBytes());
            out.flush();

            return readResponse(conn);
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "Failed to send POST request", e);
            return null;
        }

    }

    public HttpResponse send(RequestBuilder request) {
        Log.d(getClass().getSimpleName(), "Sending request " + request);
        HttpResponse res = null;

        try {
            String method = request.getMethod().toUpperCase();
            String query = getParamString(request.getParams());
            String path = request.getPath();
            HttpURLConnection conn;
            switch (method) {

                case "GET":
                    if (!query.isEmpty()) path += "?" + query;
                    conn = openConnection(path, method, request.getHeaders());
                    res = readResponse(conn);
                    break;

                case "POST":
                    conn = openConnection(path, method, request.getHeaders());
                    conn.setDoOutput(true);
                    OutputStream out = conn.getOutputStream();
                    out.write(query.getBytes());
                    out.flush();
                    res = readResponse(conn);
                    break;

                default:
                    Log.e(getClass().getSimpleName(), "Unsupported request method: " + method);
            }
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "Failed to send request " + request, e);
        }

        return res;
    }

    private String getParamString(Map<String, Object> params) throws JsonProcessingException {
        String str = "";
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (!str.isEmpty()) str += "&";
            Object value = entry.getValue();
            str += entry.getKey() + "=" + (value instanceof String ? value : mapper.writeValueAsString(value));
        }
        return str;
    }

    private HttpURLConnection openConnection(String path, String method, Map<String, String> headers) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(serverUrl + path).openConnection();
        conn.setRequestMethod(method);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            conn.setRequestProperty(entry.getKey(), entry.getValue());
        }
        return conn;
    }

    private HttpResponse readResponse(HttpURLConnection conn) throws IOException {
        int code = conn.getResponseCode();
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        String body = "";
        String line;
        while ((line = br.readLine()) != null) body += line;

        return new HttpResponse(code, body, conn.getHeaderFields());
    }

}
