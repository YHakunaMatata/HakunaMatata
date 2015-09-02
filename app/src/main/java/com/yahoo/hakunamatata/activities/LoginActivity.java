package com.yahoo.hakunamatata.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.fragments.FacebookFragment;
import com.yahoo.hakunamatata.models.User;
import com.yahoo.hakunamatata.network.FacebookClient;
import com.yahoo.hakunamatata.network.MyJsonHttpResponseHandler;
import com.yahoo.hakunamatata.storage.Storage;

import org.apache.http.Header;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements FacebookFragment.oauthCallBack {

    private FacebookFragment facebookFragment;


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);

        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            facebookFragment = new FacebookFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, facebookFragment)
                    .commit();
        } else {
            // Or set the fragment from restored state info
            facebookFragment = (FacebookFragment) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }

        if (Storage.read(this, "accessToken", "").length() > 0) {
            Log.e("access token", Storage.read(this, this.getResources().getString(R.string.access_token), ""));
            getMe();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public void onSuccess(AccessToken accessToken) {
        Storage.write(this, this.getResources().getString(R.string.access_token), accessToken.getToken());
        getMe();
    }

    public void navigateToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onFailed() {

    }

    public void getMe() {
        FacebookClient client = RestApplication.getRestClient();
        client.getMe(new MyJsonHttpResponseHandler(this) {
            @Override
            public void successCallBack(int statusCode, Header[] headers, Object data) {
                JSONObject dataJSON = (JSONObject) data;
                User user = User.fromJSON(dataJSON.toString());
                RestApplication.setMe(user);
                navigateToMainActivity();
            }

            @Override
            public void errorCallBack() {

            }
        });
    }
}
