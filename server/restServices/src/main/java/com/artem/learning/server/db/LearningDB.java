package com.artem.learning.server.db;

import com.artem.learning.server.model.UniqueEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * TODO: Document!
 *
 * @author artem
 *         Date: 2/27/16
 */
@Component
public class LearningDB {

    public static final String DB_URL = "http://localhost:5984/learning";

    private ObjectMapper mapper = new ObjectMapper();

    public void addDrawing(String drawingJsonStr) {
        String idPathParam = "id";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(idPathParam, UUID.randomUUID().toString());
        new RestTemplate().put(DB_URL + "/{" + idPathParam + "}", drawingJsonStr, paramMap);
    }

    public void createEntity(Object entity) {
        String idPathParam = "id";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(idPathParam, UUID.randomUUID().toString());
        try {
            new RestTemplate().put(DB_URL + "/{" + idPathParam + "}", mapper.writeValueAsString(entity), paramMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize entity of type " + entity.getClass().getName(), e);
        }
    }

    public <T> T[] find(String viewPath, String key, Class<T> entityType) {
        String bodyStr = new RestTemplate().getForEntity(DB_URL + viewPath + "?key=\"" + key + "\"", String.class).getBody();
        String prefix = "\"rows\":";
        String rowsStr = bodyStr.substring(bodyStr.indexOf(prefix) + prefix.length(), bodyStr.length() - 1);

        try {
            return (T[]) mapper.readValue(rowsStr, Array.newInstance(entityType, 0).getClass());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(UniqueEntity entity) {
        new RestTemplate().delete(DB_URL + "/" + entity.getId());
    }
}
