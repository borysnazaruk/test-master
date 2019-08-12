package com.test.ws.restclient.parser;


import com.google.gson.Gson;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import com.test.engine.httpclient.HttpResponseWrapper;

import java.lang.reflect.Type;

public abstract class Parser {

    public static ResponseError parseResponseError(final HttpResponseWrapper response) {
        return fromJson(response.getBody(), ResponseError.class);
    }

    public static <T extends Object> T fromJson(final String json, final Class<T> returnType) {
        return new Gson().fromJson(json, returnType);
    }

    public static <T extends Object> T fromJson(final String json, final Type type) {
        return new Gson().fromJson(json, type);
    }

    public static String toJson(final Object o) {
        return new Gson().toJson(o);
    }

    public static String toJson(final Object o, JsonWriter writer) {
        Gson gson = new Gson();
        gson.toJson(o, o.getClass(), writer);
        return ((JsonTreeWriter) writer).get().toString();
    }

    public static String toJson(final Object o, final Type type) {
        return new Gson().toJson(o, type);
    }
}
