package com.artem.learning.server.couchdb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

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

    /**
     * Add a new document to the DB.
     *
     * @param doc the document to be added. In case of success its persistent fields <code>id</code> and <code>revision</code> are updated.
     * @return the insert response
     */
    public UpdateDocumentResponse addDocument(Document doc) {
        UpdateDocumentResponse res = getRestTemplate().postForObject(dbUrl(), doc, UpdateDocumentResponse.class);
        if (res.isSuccess()) doc.updatePersistentProperties(res);
        return res;
    }

    /**
     * Retrieve a document by id.
     *
     * @param id  document id
     * @param cls document class
     * @param <T> document type as defined by cls parameter
     * @return the retrieved document of given type, null if the id does not exist
     */
    public <T extends Document> T getDocument(String id, Class<T> cls) {
        try {
            return getRestTemplate().getForObject(dbUrl() + "/" + id, cls);
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND)
                return null;
            else
                throw e;
        }
    }

    /**
     * Update the document in the DB.
     *
     * @param doc the document to updated. In case of success its persistent fields <code>revision</code> is updated.
     * @return the update response
     */
    public UpdateDocumentResponse updateDocument(Document doc) {
        String[] revisionHeader = null;
        if (doc.getRevision() != null) {
            revisionHeader = new String[]{"If-Match", doc.getRevision()};
        }
        try {
            ResponseEntity<UpdateDocumentResponse> responseEntity = getRestTemplate(revisionHeader)
                    .exchange(dbUrl() + "/" + doc.getId(), HttpMethod.PUT, new HttpEntity<>(doc), UpdateDocumentResponse.class);
            UpdateDocumentResponse res = responseEntity.getBody();
            if (res.isSuccess()) doc.updatePersistentProperties(res);
            return res;
        } catch (HttpStatusCodeException e) {
            return new UpdateDocumentResponse(false, e.getStatusCode());
        }
    }

    public UpdateDocumentResponse deleteDocument(Document doc) {
        try {
            String[] revisionHeader = {"If-Match", doc.getRevision()};
            getRestTemplate(revisionHeader).delete(dbUrl() + "/" + doc.getId());
            return new UpdateDocumentResponse(true, null);
        } catch (HttpStatusCodeException e) {
            return new UpdateDocumentResponse(false, e.getStatusCode());
        }
    }

    public <T> View<T> getView(String designDocName, String viewName, Class<T> valueClass) {
        return new View<>(this, designDocName, viewName, valueClass);
    }

    RestTemplate getRestTemplate(String[]... requestHeaders) {
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

    String dbUrl() {
        return couchDB + "/" + dbName;
    }
}
