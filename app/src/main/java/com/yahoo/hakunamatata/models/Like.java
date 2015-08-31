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
public class Like {
    public int total_count;
}
