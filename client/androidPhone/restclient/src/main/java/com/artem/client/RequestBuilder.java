package com.artem.client;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by artem on 3/30/16.
 */
public class RequestBuilder {

    private String method;
    private String path;
    private Map<String, Object> params = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private ResponseListener listener;

    private RequestBuilder(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public static RequestBuilder get(String path) {
        return new RequestBuilder("GET", path);
    }

    public static RequestBuilder post(String path) {
        return new RequestBuilder("POST", path);
    }

    public RequestBuilder param(String name, Object value) {
        params.put(name, value);
        return this;
    }

    public RequestBuilder header(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public RequestBuilder onResponse(ResponseListener listener) {
        this.listener = listener;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public ResponseListener getListener() {
        return listener;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public String getPath() {
        return path;
    }

    public ResponseListener getResponseListener() {
        return listener;
    }

    @Override
    public String toString() {
        return method + " " + path;
    }
}
