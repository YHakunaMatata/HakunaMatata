package com.yahoo.hakunamatata.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.fragments.BaseFragment;
import com.yahoo.hakunamatata.fragments.FavoriteFragment;
import com.yahoo.hakunamatata.fragments.JokeFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jonaswu on 2015/8/18.
 */
public class ListingPagerAdapter extends FragmentPagerAdapter implements SmartTabLayout.TabProvider, ViewPager.OnPageChangeListener {
    final int PAGE_COUNT = 2;
    private final FragmentManager fm;
    private String tabTitles[] = new String[]{"JOKES", "FAVORITE"};
    private Map<Integer, Fragment> mFragmentTags = new HashMap<>();

    private final Context context;

    public ListingPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.fm = fm;
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        if (obj instanceof Fragment) {
            // record the fragment tag here.
            switch (position) {
                case 0:
                    mFragmentTags.put(0, (Fragment) obj);
                    break;
                case 1:
                    mFragmentTags.put(1, (Fragment) obj);
                    break;
                case 2:
                    mFragmentTags.put(2, (Fragment) obj);
                    break;
            }
        }
        return obj;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag;
        switch (position) {
            case 0:
                frag = JokeFragment.newInstance();
                break;
            case 1:
                frag = FavoriteFragment.newInstance();
                break;
            default:
                frag = JokeFragment.newInstance();
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public View createTabView(ViewGroup viewGroup, int i, PagerAdapter pagerAdapter) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tab_layout, viewGroup, false);
        TextView text = (TextView) view.findViewById(R.id.text);
        ImageView image = (ImageView) view.findViewById(R.id.image);
        text.setText(tabTitles[i]);
        if (i == 0) {
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.funny));
        } else if (i == 1) {
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.folder265));

        }
        return view;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        ((BaseFragment) mFragmentTags.get(position)).reload();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public Fragment getFragment(int position) {
        return mFragmentTags.get(position);
    }

}