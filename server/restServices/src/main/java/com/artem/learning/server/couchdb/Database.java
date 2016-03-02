package com.artem.learning.server.couchdb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * CouchDB client with jackson serialization/deserialization
 *
 * @author artem on 2/28/16.
 */

@Component
public class Database {

    @Value("${couchdb.url}")
    private String couchDB;

    @Value("${couchdb.database}")
    private String dbName;

    public ConnectionInfo getConnectionInfo() {
        return getRestTemplate().getForObject(couchDB + "/", ConnectionInfo.class);
    }

    public DatabaseInfo getDatabaseInfo() {
        try {
            return getRestTemplate().getForObject(dbUrl(), DatabaseInfo.class);
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND)
                return null;
            else
                throw e;
        }
    }

    public DatabaseInfo createDB() {
        getRestTemplate().put(dbUrl(), null);
        return getDatabaseInfo();
    }

    public void deleteDB() {
        getRestTemplate().delete(dbUrl());
    }

    public UpdateResponse addDocument(Object obj) {
        return getRestTemplate().postForObject(dbUrl(), obj, UpdateResponse.class);
    }

    public <T> T getDocument(String id, Class<T> cls) {
        try {
            return getRestTemplate().getForObject(dbUrl() + "/" + id, cls);
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND)
                return null;
            else
                throw e;
        }
    }

    public UpdateResponse updateDocument(String id, String revision, Object obj) {
        String[] revisionHeader = {"If-Match", revision};
        try {
            ResponseEntity<UpdateResponse> responseEntity = getRestTemplate(revisionHeader)
                    .exchange(dbUrl() + "/" + id, HttpMethod.PUT, new HttpEntity<>(obj), UpdateResponse.class);
            return responseEntity.getBody();
        } catch (HttpStatusCodeException e) {
            return new UpdateResponse(false, e.getStatusCode());
        }
    }

    public UpdateResponse deleteDocument(String id, String revision) {
        try {
            String[] revisionHeader = {"If-Match", revision};
            getRestTemplate(revisionHeader).delete(dbUrl() + "/" + id + "?rev=" + revision);
            return new UpdateResponse(true, null);
        } catch (HttpStatusCodeException e) {
            return new UpdateResponse(false, e.getStatusCode());
        }
    }

    public <T> ViewResponse<T> findByKey(String designDocName, String viewName, Class<T> cls, String startKey, String endKey) {
        String url = dbUrl() + "/_design/" + designDocName + "/_view/" + viewName;
        String queryParam = "";
        if (startKey != null) queryParam += "startkey=\"" + startKey + "\"";
        if (endKey != null) queryParam += "endkey=\"" + endKey + "\"";
        if (!queryParam.isEmpty()) url += "?" + queryParam;

        ValueDeserializer.setValueClass(cls);
        ViewResponse response = getRestTemplate().getForObject(url, ViewResponse.class);
        ValueDeserializer.removeValueClass();
        return response;
    }


    public void createOrUpdateDesignDocument(String designDocName, ViewDefinition... viewDefinitions) {
        String id = "/_design/" + designDocName;
        Map design = getDocument(id, Map.class);
        String revision = design == null ? null : (String) design.get("_rev");

        if (design == null) {
            design = new HashMap<>();
            design.put("language", "javascript");
        }

        Map views = (Map) design.get("views");
        if (views == null) {
            views = new HashMap<>();
            design.put("views", views);
        }

        for (ViewDefinition definition : viewDefinitions) {
            Map view = (Map) views.get(definition.name);
            if (view == null) {
                view = new HashMap<>();
                views.put(definition.name, view);
            }
            view.put("map", definition.getMapFunction());
            view.put("reduce", definition.getReduceFunction());
        }

        updateDocument(id, revision, design);
    }

    private RestTemplate getRestTemplate(String[]... requestHeaders) {
        RestTemplate res = new RestTemplate();
        if (requestHeaders != null) {
            res.setInterceptors(Collections.singletonList((ClientHttpRequestInterceptor) (request, body, execution) -> {
                HttpRequest wrapper = new HttpRequestWrapper(request);
                HttpHeaders headers = wrapper.getHeaders();
                for (String[] pair : requestHeaders) {
                    if (pair != null) headers.set(pair[0], pair[1]);
                }
                return execution.execute(wrapper, body);
            }));
        }
        return res;
    }

    private String dbUrl() {
        return couchDB + "/" + dbName;
    }
}
