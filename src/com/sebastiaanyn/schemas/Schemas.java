package com.sebastiaanyn.schemas;

import java.util.HashMap;
import java.util.Map;

public class Schemas {

    private static final Map<String, Class> schemas = new HashMap<>();

    static {
        schemas.put("message", Message.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getSchema(String name) {
        return (Class<T>) schemas.get(name.toLowerCase());
    }
}
