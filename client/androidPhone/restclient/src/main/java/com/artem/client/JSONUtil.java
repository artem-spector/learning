package com.artem.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by artem on 4/2/16.
 */
public class JSONUtil {

    private ObjectMapper mapper = new ObjectMapper();
    private TypeFactory typeFactory = TypeFactory.defaultInstance();

    public String marshal(Object value) throws JsonProcessingException {
        return mapper.writeValueAsString(value);
    }

    public <T> T unmarshalValue(String str, Class<T> type) throws IOException {
        return mapper.readValue(str, type);
    }

    public <T> List<T> unmarshalList(String str, Class<T> elementType) throws IOException {
        JavaType listType = typeFactory.constructCollectionType(List.class, elementType);
        return mapper.readValue(str, listType);
    }

}
