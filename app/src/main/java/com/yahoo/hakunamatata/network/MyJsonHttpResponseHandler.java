package com.yahoo.hakunamatata.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.hakunamatata.R;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by jonaswu on 2015/8/11.
 */
public abstract class MyJsonHttpResponseHandler extends JsonHttpResponseHandler {

    private Context context;

    public MyJsonHttpResponseHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray data) {
        successCallBack(statusCode, headers, data);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
        successCallBack(statusCode, headers, data);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject data) {
        Log.e("error", "on JSONObject failure");
        Log.e("statusCode", String.valueOf(statusCode));
        Log.e("headers", headers.toString());
        Toast.makeText(context, context.getResources().getString(R.string.networkerror), Toast.LENGTH_LONG).show();
        errorCallBack();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray data) {
        Log.e("error", "on JSONObject failure");
        Log.e("statusCode", String.valueOf(statusCode));
        Log.e("headers", headers.toString());
        Toast.makeText(context, context.getResources().getString(R.string.networkerror), Toast.LENGTH_LONG).show();
        errorCallBack();
    }

    public abstract void successCallBack(int statusCode, Header[] headers, Object data);

    public abstract void errorCallBack();
}
