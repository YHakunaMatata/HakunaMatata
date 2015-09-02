package com.yahoo.hakunamatata.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

/**
 * Created by jonaswu on 2015/8/31.
 */
public class User {

    public String id;
    public String name;
    public Picture picture;

    public static User fromJSON(String data) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(Picture.class, new Deserializer<Picture>() {
                    @Override
                    public JsonElement getDeserializeData(JsonElement je) {
                        return je.getAsJsonObject().get("data");
                    }
                })
                .create();
        return gson.fromJson(data, User.class);
    }
}

