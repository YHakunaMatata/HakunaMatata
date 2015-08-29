package com.yahoo.hakunamatata.network;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.storage.Storage;

/**
 * Created by jonaswu on 2015/8/29.
 */
public class FacebookClient {
    public static FacebookClient facebookClient;
    public static String baseUrl = "https://graph.facebook.com/v2.4/";
    public static String groupId = "173830556282081";
    public Context context;

    public static FacebookClient getInstance(Context context) {
        if (facebookClient == null) {
            facebookClient = new FacebookClient(context);
        }
        return facebookClient;
    }

    public FacebookClient(Context context) {
        this.context = context;
    }

    // RestClient.java
    public void getPosts(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(String.format("%s/feed", groupId));
        RequestParams params = new RequestParams();
        params.put("fields", "type,id,message,from.fields(name, cover, picture)");
        getClient().get(apiUrl, decorateParams(params, "get"), handler);
    }

    // RestClient.java
    public void getPhotos(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(String.format("%s/feed", groupId));
        RequestParams params = new RequestParams();
        getClient().get(apiUrl, decorateParams(params, "get"), handler);
    }

    // RestClient.java
    public void getVideos(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(String.format("%s/feed", groupId));
        RequestParams params = new RequestParams();
        getClient().get(apiUrl, decorateParams(params, "get"), handler);
    }

    // RestClient.java
    public void post(String message, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(String.format("%s/feed", groupId));
        RequestParams params = new RequestParams();
        params.put("message", message);
        getClient().post(apiUrl, decorateParams(params, "post"), handler);
    }

    private String getApiUrl(String path) {
        return baseUrl + path;
    }

    private AsyncHttpClient getClient() {
        AsyncHttpClient client = new AsyncHttpClient();
        return client;
    }

    private RequestParams decorateParams(RequestParams params, String method) {
        String accessToken = Storage.read(context, context.getResources().getString(R.string.access_token), "");
        params.put("access_token", accessToken);
        params.put("debug", "all");
        params.put("format", "json");
        params.put("method", method);
        params.put("pretty", 0);
        params.put("suppress_http_code", 1);

        return params;
    }

}
