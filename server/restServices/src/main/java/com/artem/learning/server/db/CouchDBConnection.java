package com.artem.learning.server.db;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Document!
 *
 * @author artem on 2/28/16.
 */

@Component
public class CouchDBConnection {

    public static final String URL = "http://localhost:5984";

    public CouchDBInfo getDBInfo() {
        return getRestTemplate(null).getForObject(URL + "/", CouchDBInfo.class);
    }

    public CouchDB getDB(String dbName) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        String body = null;
        try {
            body = getRestTemplate(headers).getForEntity(URL + "/" + dbName, String.class).getBody();
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND)
                return null;
        }
        return new CouchDB(dbName);
    }

    public CouchDB createDB(String dbName) {
        new RestTemplate().put(URL + "/" + dbName, null);
        return new CouchDB(dbName);
    }

    public void deleteDB(String dbName) {
        new RestTemplate().delete(URL + "/" + dbName);
    }

    private RestTemplate getRestTemplate(Map<String, String> requestHeaders) {
        RestTemplate res = new RestTemplate();
        if (requestHeaders != null && !requestHeaders.isEmpty()) {
            res.setInterceptors(Collections.singletonList((ClientHttpRequestInterceptor) (request, body, execution) -> {
                HttpRequest wrapper = new HttpRequestWrapper(request);
                HttpHeaders headers = wrapper.getHeaders();
                for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                    headers.set(entry.getKey(), entry.getValue());
                }
                return execution.execute(wrapper, body);
            }));
        }
        return res;
    }
}
