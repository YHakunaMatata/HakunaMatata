package com.yahoo.hakunamatata.activities;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.dk.view.folder.ResideMenu;
import com.dk.view.folder.ResideMenuItem;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.adapters.ListingPagerAdapter;
import com.yahoo.hakunamatata.fragments.SubmitFragment;
import com.yahoo.hakunamatata.interfaces.Reloadable;
import com.yahoo.hakunamatata.storage.Storage;

import java.io.IOException;


public class MainActivity extends BaseActivity implements SubmitFragment.PostSuccessDelegator  {
    private ListingPagerAdapter adapter;
    private ViewPager viewPager;
    private SmartTabLayout viewPagerTab;

    private ResideMenu resideMenu;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemProfile;
    private ResideMenuItem itemCalendar;
    private ResideMenuItem itemSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpPager();
        setUpMenu();
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
        itemHome = new ResideMenuItem(this, R.drawable.icon_home, "Home");
        itemProfile = new ResideMenuItem(this, R.drawable.icon_profile, "Gallery");
        itemCalendar = new ResideMenuItem(this, R.drawable.icon_calendar, "Calendar");
        itemSettings = new ResideMenuItem(this, R.drawable.icon_settings, "Settings");


        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemCalendar, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_RIGHT);

        // You can disable a direction by setting ->
        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
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
