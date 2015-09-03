package com.yahoo.hakunamatata.network;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.lib.util;
import com.yahoo.hakunamatata.models.FacebookPaging;
import com.yahoo.hakunamatata.storage.Storage;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by jonaswu on 2015/8/29.
 */
public class FacebookClient {
    public static FacebookClient facebookClient;
    public static String baseUrl = "https://graph.facebook.com/v2.4/";
    public static String groupId = "yhakunamatatagroup";
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
    public void getPosts(FacebookPaging facebookPaging, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(String.format("%s/feed", groupId));
        RequestParams params = new RequestParams();
        params.put("fields", "likes.summary(true),created_time,link,type,id,picture,full_picture,message,from.fields(name, cover, picture)");
        if (facebookPaging != null) {
            Log.e("next", facebookPaging.next);
            try {
                Map<String, List<String>> query = util.splitQuery(new URL(facebookPaging.next));
                params.put("__paging_token", query.get("__paging_token").get(0));
                params.put("until", query.get("until").get(0));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        getClient().get(apiUrl, decorateParams(params, "get"), handler);
    }


    // RestClient.java
    public void getCommentsOfObject(String id, FacebookPaging facebookPaging, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(String.format("%s/comments", id));
        RequestParams params = new RequestParams();
        params.put("fields", "message,id,from.fields(name, cover, picture)");
        if (facebookPaging != null) {
            try {
                Map<String, List<String>> query = util.splitQuery(new URL(facebookPaging.next));
                params.put("after", facebookPaging.after);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        getClient().get(apiUrl, decorateParams(params, "get"), handler);
    }

    // RestClient.java
    public void getMe(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("me");
        RequestParams params = new RequestParams();
        params.put("fields", "name,cover,picture");
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

    public void postPhoto(String message, byte[] byteImage, GraphRequest.Callback cb) {
        String path = String.format("%s/photos", groupId);
        AccessToken at = AccessToken.getCurrentAccessToken();
        Bundle parameters = new Bundle();
        parameters.putString("message", message);
        parameters.putByteArray("picture", byteImage);
        HttpMethod method = HttpMethod.POST;
        GraphRequest request = new GraphRequest(at, path, parameters, method, cb);
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void postComment(String postId, String message, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(String.format("%s/comments", postId));
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
        params.put("limit", context.getResources().getString(R.string.limit_of_api_return));
        params.put("format", "json");
        params.put("method", method);
        params.put("pretty", 0);
        params.put("suppress_http_code", 1);
        return params;
    }

}
