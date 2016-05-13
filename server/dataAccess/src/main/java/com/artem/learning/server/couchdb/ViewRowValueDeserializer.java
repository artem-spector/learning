package com.artem.learning.server.couchdb;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * TODO: Document!
 *
 * @author artem on 2/29/16.
 */
public class ViewRowValueDeserializer extends JsonDeserializer {

    private static ThreadLocal<Class> valueType = new ThreadLocal<>();

    public static void setValueClass(Class cls) {
        valueType.set(cls);
    }

    public static void clear() {
        valueType.remove();
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.readValueAs(valueType.get());
    }
}


