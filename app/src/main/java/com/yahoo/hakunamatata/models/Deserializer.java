package com.yahoo.hakunamatata.models;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by jonaswu on 2015/8/31.
 */
public abstract class Deserializer<T> implements JsonDeserializer<T> {
    public abstract JsonElement getDeserializeData(JsonElement je);

    @Override
    public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException {
        JsonElement data = getDeserializeData(je);
        return new Gson().fromJson(data, type);
    }
}
