package com.yahoo.hakunamatata.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

/**
 * Created by jonaswu on 2015/8/30.
 */
public class Post {
    public Like likes;
    public User from;
    public String id;
    public String message;
    public String type;
    public String picture;
    public String full_picture;
    public String link;

    public static Post fromJSON(String data) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(Picture.class, new Deserializer<Picture>() {
                    @Override
                    public JsonElement getDeserializeData(JsonElement je) {
                        return je.getAsJsonObject().get("data");
                    }
                })
                .registerTypeAdapter(Like.class, new Deserializer<Like>() {
                    @Override
                    public JsonElement getDeserializeData(JsonElement je) {
                        return je.getAsJsonObject().get("summary");
                    }
                })
                .create();
        return gson.fromJson(data.toString(), Post.class);
    }
}
