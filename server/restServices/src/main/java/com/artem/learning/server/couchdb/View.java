package com.artem.learning.server.couchdb;

/**
 * TODO: Document!
 *
 * @author artem on 3/2/16.
 */
public class View<ValueType> {

    private Database db;
    private String designDocName;
    private String viewName;
    private Class<ValueType> valueClass;

    View(Database db, String designDocName, String viewName, Class<ValueType> valueClass) {
        this.db = db;
        this.designDocName = designDocName;
        this.viewName = viewName;
        this.valueClass = valueClass;
    }

    public LookupViewResponse<ValueType> lookup(Object startKey, Object endKey) {
        String url = db.dbUrl() + "/_design/" + designDocName + "/_view/" + viewName;
        String queryParam = "";
        if (startKey != null) queryParam += "startkey=\"" + startKey + "\"";
        if (endKey != null) {
            if (startKey != null) queryParam += "&";
            queryParam += "endkey=\"" + endKey + "\"";
        }
        if (!queryParam.isEmpty()) url += "?" + queryParam;

        ViewRowValueDeserializer.setValueClass(valueClass);
        LookupViewResponse<ValueType> response = db.getRestTemplate().getForObject(url, LookupViewResponse.class);
        ViewRowValueDeserializer.clear();
        return response;
    }

}


