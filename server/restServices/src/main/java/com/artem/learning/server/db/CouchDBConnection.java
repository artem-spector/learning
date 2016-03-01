package com.artem.learning.server.db;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
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

    public CouchDBConnectionInfo getConnectionInfo() {
        return getRestTemplate(null).getForObject(URL + "/", CouchDBConnectionInfo.class);
    }

    public CouchDBInfo getDB(String dbName) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        try {
            return getRestTemplate(headers).getForObject(URL + "/" + dbName, CouchDBInfo.class);
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND)
                return null;
            else
                throw e;
        }
    }

    public CouchDBInfo createDB(String dbName) {
        new RestTemplate().put(URL + "/" + dbName, null);
        return getDB(dbName);
    }

    public void deleteDB(String dbName) {
        new RestTemplate().delete(URL + "/" + dbName);
    }

    public UpdateResponse addObject(String dbName, Object obj) {
        return new RestTemplate().postForObject(URL + "/" + dbName, obj, UpdateResponse.class);
    }

    public <T> T getObject(String dbName, String id, Class<T> cls) {
        try {
            return new RestTemplate().getForObject(URL + "/" + dbName + "/" + id, cls);
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND)
                return null;
            else
                throw e;
        }
    }

    public UpdateResponse updateObject(String dbName, String id, String revision, Object obj) {
        Map<String, String> headers = new HashMap<>();
        headers.put("If-Match", revision);
        ResponseEntity<UpdateResponse> responseEntity =
                getRestTemplate(headers).exchange(URL + "/" + dbName + "/" + id, HttpMethod.PUT, new HttpEntity<>(obj), UpdateResponse.class);
        return responseEntity.getBody();
    }

    public void deleteObject(String dbName, String id, String revision) {
        new RestTemplate().delete(URL + "/" + dbName + "/" + id + "?rev=" + revision);
    }

    public <T> ViewResponse<T> findByKey(String dbName, String designDocName, String viewName, Class<T> cls, String startKey, String endKey) {
        String url = URL + "/" + dbName + "/_design/" + designDocName + "/_view/" + viewName;
        String queryParam = "";
        if (startKey != null) queryParam += "startkey=\"" + startKey + "\"";
        if (endKey != null) queryParam += "endkey=\"" + endKey + "\"";
        if (!queryParam.isEmpty()) url += "?" + queryParam;

        ValueDeserializer.setValueClass(cls);
        ViewResponse response = new RestTemplate().getForObject(url, ViewResponse.class);
        ValueDeserializer.removeValueClass();
        return response;
    }

    public void setView(String dbName, String designDocName, String viewName, String mapFunctionFile, String reduceFunctionFile) {
        Map design = getObject(dbName, "/_design/" + designDocName, Map.class);
        if (design == null) {
            design = new HashMap<>();
            design.put("language", "javascript");
        }

        Map views = (Map) design.get("views");
        if (views == null) {
            views = new HashMap<>();
            design.put("views", views);
        }

        Map view = (Map) views.get(viewName);
        if (view == null) {
            view = new HashMap<>();
            views.put(viewName, view);
        }

        try {
            view.put("map", IOUtils.toString(new ClassPathResource(mapFunctionFile).getInputStream()));
            view.put("reduce", reduceFunctionFile == null ? null
                    : IOUtils.toString(new ClassPathResource(reduceFunctionFile).getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the contents of view functions", e);
        }

        new RestTemplate().put(URL + "/" + dbName + "/_design/" + designDocName, design);
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
