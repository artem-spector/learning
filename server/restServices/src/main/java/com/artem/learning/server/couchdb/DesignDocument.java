package com.artem.learning.server.couchdb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Design document
 *
 * @author artem on 3/5/16.
 */
public class DesignDocument extends Document {

    @JsonProperty("language")
    private String language;

    @JsonProperty("views")
    private Map<String, Map<String, String>> views;

    public DesignDocument(String docName, String language, ViewDefinition... viewDefinitions) {
        super("_design/" + docName);
        this.language = language;
        views = new HashMap<>();
        if (viewDefinitions != null) {
            for (ViewDefinition viewDefinition : viewDefinitions) {
                Map<String, String> functions = new HashMap<>();
                functions.put("map", viewDefinition.getMapFunction());
                functions.put("reduce", viewDefinition.getReduceFunction());
                views.put(viewDefinition.name, functions);
            }
        }
    }

}
