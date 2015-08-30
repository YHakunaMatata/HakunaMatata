package com.yahoo.hakunamatata.element;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by jonaswu on 2015/8/30.
 */
public class Post {

    public abstract static class Deserializer<T> implements JsonDeserializer<T> {

        public abstract JsonElement getDeserializeData(JsonElement je);

        @Override
        public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
                throws JsonParseException {
            JsonElement data = getDeserializeData(je);
            return new Gson().fromJson(data, type);
        }

    }

    public static class From {
        public static class Picture {
            public String url;
        }

        public String id;
        public String name;
        public Picture picture;
    }

    public From from;
    public String message;
}
