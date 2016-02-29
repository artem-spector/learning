package com.artem.learning.server.db;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * TODO: Document!
 *
 * @author artem on 2/29/16.
 */
public class ValueDeserializer extends JsonDeserializer {

    private static ThreadLocal<Class> valueType = new ThreadLocal<>();

    public static void setValueClass(Class cls) {
        valueType.set(cls);
    }

    public static void removeValueClass() {
        valueType.remove();
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return p.readValueAs(valueType.get());
    }
}


