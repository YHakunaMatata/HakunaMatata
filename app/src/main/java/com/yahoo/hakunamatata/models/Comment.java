package com.yahoo.hakunamatata.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.util.Date;

/**
 * Created by jonaswu on 2015/9/3.
 */
public class Comment {
    public User from;
    public String id;
    public String message;
    public Date created_time;


    public static Comment fromJSON(String data) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss+SSSS")
                .registerTypeAdapter(Picture.class, new Deserializer<Picture>() {
                    @Override
                    public JsonElement getDeserializeData(JsonElement je) {
                        return je.getAsJsonObject().get("data");
                    }
                })
                .create();
        return gson.fromJson(data.toString(), Comment.class);
    }
}
