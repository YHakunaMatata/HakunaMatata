package com.yahoo.hakunamatata.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.widget.Toast;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.adapters.ListingPagerAdapter;
import com.yahoo.hakunamatata.storage.Storage;


public class MainActivity extends BaseActivity {
    private ListingPagerAdapter adapter;
    private ViewPager viewPager;
    private SmartTabLayout viewPagerTab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpPager();
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

    public void onClickPost(MenuItem item) {
        Toast.makeText(this, "Click Post", Toast.LENGTH_SHORT).show();
    }

    public void logout(MenuItem item) {
        Storage.write(this, this.getResources().getString(R.string.access_token), "");
        finish();
    }

}
