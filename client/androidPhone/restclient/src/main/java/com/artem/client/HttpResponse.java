package com.artem.client;

import java.util.List;
import java.util.Map;

/**
 * created by artems on 2/22/16.
 */
public class HttpResponse {

    public final int status;
    public final String body;
    public final Map<String, List<String>> headers;

    public HttpResponse(int status, String body, Map<String, List<String>> headers) {
        this.status = status;
        this.body = body;
        this.headers = headers;
    }
}
