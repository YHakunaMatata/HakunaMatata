package com.yahoo.hakunamatata.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.fragments.FacebookFragment;

public class LoginActivity extends ActionBarActivity implements FacebookFragment.oauthCallBack {


    private FacebookFragment facebookFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        if (AccessToken.getCurrentAccessToken() == null || AccessToken.getCurrentAccessToken().isExpired()) {
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
        } else {
            navigateToMainActivity();
        }

    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.facebook_login);
    }*/


    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);

        return true;
    }

    @Override
    public void onSuccess(String accessToken) {
        navigateToMainActivity();
    }

    public void navigateToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onFailed() {

    }
}
