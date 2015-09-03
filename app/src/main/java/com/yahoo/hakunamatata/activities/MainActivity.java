package com.yahoo.hakunamatata.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.dk.view.folder.ResideMenu;
import com.dk.view.folder.ResideMenuItem;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.adapters.ListingPagerAdapter;
import com.yahoo.hakunamatata.fragments.AboutFragment;
import com.yahoo.hakunamatata.fragments.SubmitFragment;
import com.yahoo.hakunamatata.interfaces.Reloadable;
import com.yahoo.hakunamatata.pager.CustomViewPager;
import com.yahoo.hakunamatata.storage.Storage;

public class MainActivity extends BaseActivity implements SubmitFragment.PostSuccessDelegator {
    private ListingPagerAdapter adapter;
    private CustomViewPager viewPager;
    private SmartTabLayout viewPagerTab;

    private Context mContext;
    private ResideMenu resideMenu;
    private ResideMenuItem menuItemGuide;
    private ResideMenuItem menuItemAbout;
    private ResideMenuItem menuItemSetting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SharedPreference->HakunaMatata->isFirstLaunch
        // User will be direct to guide activity only when the first time launch the app.
        if (getSharedPreferences("HakunaMatata", 0).getBoolean("isFirstLaunch", true)) {
            navigateToGuideActivity();
        }

        mContext = this;
        setUpPager();
        setUpMenu();
    }

    // App would crash if Home Button is pressed when the reside Menu is opened,
    // Error: can't get Toolbar, Title blablabla...
    // haven't find the root cause yet
    @Override
    protected void onStop() {
        if(resideMenu != null && resideMenu.isOpened()) {
            resideMenu.closeMenu();
        }
        super.onStop();
    }


    private void setUpPager() {
        adapter = new ListingPagerAdapter(
                getSupportFragmentManager(),
                this
        );
        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setCustomTabView(adapter);
        viewPagerTab.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(adapter);
    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.bg_menu);
        resideMenu.attachToActivity(this);
//        resideMenu.setMenuListener(menuListener);
//        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        menuItemGuide = new ResideMenuItem(this, R.drawable.ic_guide, "Guide");
        menuItemAbout = new ResideMenuItem(this, R.drawable.ic_about, "About us");
        menuItemSetting = new ResideMenuItem(this, R.drawable.ic_setting, "Setting");


        resideMenu.addMenuItem(menuItemGuide, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(menuItemAbout, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(menuItemSetting, ResideMenu.DIRECTION_RIGHT);

        // You can disable a direction by setting ->
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);

        menuItemAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutFragment aboutFragment = AboutFragment.newInstance();
                aboutFragment.show(((BaseActivity) mContext).getSupportFragmentManager(), "about dialog");
            }
        });
        menuItemGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToGuideActivity();
            }
        });
    }

    private void navigateToGuideActivity() {
        Intent i = new Intent(this, GuideActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    public void onClickPost(MenuItem item) {
        showPostDialog();
        // Toast.makeText(this, "Click Post", Toast.LENGTH_SHORT).show();
    }

    public void logout(MenuItem item) {
        Storage.write(this, this.getResources().getString(R.string.access_token), "");
        finish();
    }

    @Override
    public void postSuccess() {

    }

    @Override
    public void reload() {
        int currentPage = viewPager.getCurrentItem();
        ((Reloadable) adapter.getFragment(currentPage)).reload();
    }
}
