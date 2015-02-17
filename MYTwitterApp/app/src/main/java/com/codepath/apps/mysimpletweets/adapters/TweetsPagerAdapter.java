package com.codepath.apps.mysimpletweets.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsFragment;

/**
 * Created by nandaja on 2/16/15.
 */
public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter {


    final int PAGE_COUNT = 2;
    private String[] tabTitles = {"Home", "Mentions"};

    public TweetsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HomeTimelineFragment();
        } else if (position == 1) {
            return new MentionsFragment();
        } else {
            return null;
        }


    }

    @Override
    public int getCount() {
        return PAGE_COUNT;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
