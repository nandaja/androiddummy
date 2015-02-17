package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapters.TweetsPagerAdapter;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;

public class TimelineActivity extends ActionBarActivity implements TweetCallBack {

    ViewPager viewPager;
    TweetsPagerAdapter tweetsPageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4099FF")));
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);
        ((TextView) mCustomView.findViewById(R.id.title_text)).setText("Home");
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);


        setContentView(R.layout.activity_timeline);

        tweetsPageAdapter = new TweetsPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(tweetsPageAdapter);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_tweet) {
            postTweet();

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTweetSaved(Tweet tweet) {

        viewPager.setCurrentItem(0);

        ((TweetsListFragment) tweetsPageAdapter.getRegisteredFragment(0)).updateTweetsView(tweet);


    }


   @Override
      protected void onActivityResult(int requestCode, int resultCode, Intent data) {

          if (resultCode == 100) {

              if(data!=null) {
                  if (data.getSerializableExtra("tweet") != null) {
                      Tweet t = (Tweet) data.getSerializableExtra("tweet");
                      viewPager.setCurrentItem(0);
                      ((TweetsListFragment) tweetsPageAdapter.getRegisteredFragment(0)).updateTweetsView(t);
                  }
              }
          }
      }


    public void postTweet() {

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        TweetFragment tweetDialog = TweetFragment.newInstance("Tweet");
        tweetDialog.show(fm, "activity_tweet");

    }

    public void replyToTweet(Tweet tweet){
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        TweetFragment tweetDialog = TweetFragment.newInstance("Tweet");
        Bundle bundle = new Bundle();
        bundle.putBoolean("isReply", true);
        bundle.putSerializable("tweet", tweet);
        tweetDialog.setArguments(bundle);
        tweetDialog.show(fm, "activity_tweet");
    }

    public void reTweet(Tweet t) {

        ((TweetsListFragment) tweetsPageAdapter.getRegisteredFragment(0)).reTweet(t);

    }

    public void favorite(Tweet t, int position) {

        ((TweetsListFragment) tweetsPageAdapter.getRegisteredFragment(0)).favorite(t, position);

    }

    public void unFavorite(Tweet t, int position) {

        ((TweetsListFragment) tweetsPageAdapter.getRegisteredFragment(0)).unFavorite(t, position);

    }

    public void onProfileView(MenuItem mi){

        if (mi.getItemId() == R.id.action_profile) {

            SharedPreferences pref =
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String screenName = pref.getString("screenName", "");
            launchProfile(screenName);

        }

    }

    public void launchProfile(String screenName){


            Intent i = new Intent(this, ProfileActivity.class);
            i.putExtra("screenName", screenName);
            startActivity(i);

    }
}
