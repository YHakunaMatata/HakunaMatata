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
    public void getPostsOfGroup(String postId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(String.format("%s/posts", postId));
        Log.e("apiUrl", apiUrl);
        RequestParams params = new RequestParams();
        getClient().get(apiUrl, decorateParams(params, "get"), handler);
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
        Log.e("can I found accessToken?", accessToken);
        params.put("access_token", accessToken);
        params.put("debug", "all");
        params.put("format", "json");
        params.put("method", method);
        params.put("pretty", 0);
        params.put("suppress_http_code", 1);
        return params;
    }

}
