package com.yahoo.hakunamatata.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.fragments.JokeFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jonaswu on 2015/8/18.
 */
public class ListingPagerAdapter extends FragmentPagerAdapter implements SmartTabLayout.TabProvider {
    final int PAGE_COUNT = 3;
    private final FragmentManager fm;
    private String tabTitles[] = new String[]{"JOKES", "IMAGES", "VIDEOS"};
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
                frag = JokeFragment.newInstance();
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
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    @Override
    public View createTabView(ViewGroup viewGroup, int i, PagerAdapter pagerAdapter) {
        Log.d("my", "createTabView");
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tab_layout, viewGroup, false);
        TextView text = (TextView) view.findViewById(R.id.tvTabText);
        ImageView image = (ImageView) view.findViewById(R.id.image);
        text.setText(tabTitles[i]);
        return view;
    }

    public Fragment getFragment(int position) {
        return mFragmentTags.get(position);
    }
}