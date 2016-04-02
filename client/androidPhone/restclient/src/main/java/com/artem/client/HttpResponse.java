package com.artem.client;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * created by artems on 2/22/16.
 */
public class HttpResponse {

    private ObjectMapper mapper = new ObjectMapper();
    private TypeFactory typeFactory = TypeFactory.defaultInstance();

    public final int status;
    public final String body;
    public final Map<String, List<String>> headers;

    public HttpResponse(int status, String body, Map<String, List<String>> headers) {
        this.status = status;
        this.body = body;
        this.headers = headers;
    }

    public <T> T unmarshalValue(Class<T> type) throws IOException {
        return mapper.readValue(body, type);
    }

    public <T> List<T> unmarshalList(Class<T> elementType) throws IOException {
        JavaType listType = typeFactory.constructCollectionType(List.class, elementType);
        return mapper.readValue(body, listType);
    }

}
