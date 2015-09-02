package com.yahoo.hakunamatata.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.fragments.GuideFragment;

import java.util.ArrayList;
import java.util.List;

import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;
import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;

public class GuideActivity extends AppCompatActivity {
    ScrollerViewPager viewPager;
    Button btnSkip;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        settings = getSharedPreferences("HakunaMatata", 0);
        settings.edit().putBoolean("isFirstLaunch", false).commit();

        viewPager = (ScrollerViewPager) findViewById(R.id.view_pager);
        SpringIndicator springIndicator = (SpringIndicator) findViewById(R.id.indicator);


        PagerModelManager manager = new PagerModelManager();
        manager.addCommonFragment(GuideFragment.class, getBgRes(), getTitles());
        ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(), manager);
        viewPager.setAdapter(adapter);
        viewPager.fixScrollSpeed();

        // just set viewPager
        springIndicator.setViewPager(viewPager);

        // play laugh sound
        startMyAudioFile();
    }

    private void startMyAudioFile() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.laugh);
        mediaPlayer.start();
    }

    private List<String> getTitles(){
        ArrayList<String> al = new ArrayList<>();
        al.add("1");
        al.add("2");
        al.add("3");
        al.add("4");
        return al;
    }

    private List<Integer> getBgRes(){
        ArrayList<Integer> al = new ArrayList<>();
        al.add(R.drawable.gd1);
        al.add(R.drawable.gd2);
        al.add(R.drawable.gd3);
        al.add(R.drawable.gd4);
        return al;
    }

    public void onSkipGuide(View view) {
        navigateToMainActivity();
    }

    private void navigateToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_guide, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
