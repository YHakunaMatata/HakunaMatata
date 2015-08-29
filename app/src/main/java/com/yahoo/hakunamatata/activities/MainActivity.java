package com.yahoo.hakunamatata.activities;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.adapters.ListingPagerAdapter;
import com.yahoo.hakunamatata.fragments.ListingFragment;
import com.yahoo.hakunamatata.network.FacebookClient;
import com.yahoo.hakunamatata.network.MyJsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity implements ListingFragment.OnFragmentInteractionListener {
    private ListingPagerAdapter adapter;
    private ViewPager viewPager;
    private SmartTabLayout viewPagerTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpActionBar();
        setUpPager();


        FacebookClient client = RestApplication.getRestClient();
        client.getPostsOfGroup("173830556282081",
                new MyJsonHttpResponseHandler(this) {
                    @Override
                    public void successCallBack(int statusCode, Header[] headers, Object data) {
                        JSONObject dataJSON = (JSONObject) data;
                        Log.e("data", dataJSON.toString());
                    }

                    @Override
                    public void errorCallBack() {

                    }
                });
    }

    private void setUpActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // getSupportActionBar().setIcon(R.drawable.ic_hakuna);
//        if (getSupportActionBar().isShowing() == true) {
//            getSupportActionBar().setElevation(0);
//            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.orange3)));
//            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//            getSupportActionBar().setCustomView(R.layout.action_bar);
//        }
    }

    private void setUpPager() {
        adapter = new ListingPagerAdapter(
                getSupportFragmentManager(),
                this
        );
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setCustomTabView(adapter);
        viewPagerTab.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void onClickPost(MenuItem item) {
        Toast.makeText(this, "Click Post", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFragmentInteraction() {
        // do nothing
        return;
    }
}
