package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.adapters.SmartFragmentStatePagerAdapter;
import com.codepath.apps.mysimpletweets.fragments.FollowersFragment;
import com.codepath.apps.mysimpletweets.fragments.FollowingFragment;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;
import com.codepath.apps.mysimpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileActivity extends ActionBarActivity implements TweetCallBack {
    TwitterClient client;
    User user;

    ProgressBar pb;
    ViewPager viewPager;
    UserProfilePagerAdapter userProfileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4099FF")));
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);
        ((TextView) mCustomView.findViewById(R.id.title_text)).setText("Profile");
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        setContentView(R.layout.activity_profile);

        client = TwitterApplication.getRestClient();
        String screenName = getIntent().getExtras().getString("screenName");
        pb = (ProgressBar) findViewById(R.id.pbLoading);
        pb.setVisibility(View.VISIBLE);

        client.getUser(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] header, JSONObject response) {

                user = new User();
                user = User.buildUser(response);

                populateUserInfo(user);

                userProfileAdapter = new UserProfilePagerAdapter(getSupportFragmentManager());

                viewPager = (ViewPager) findViewById(R.id.viewpager);
                viewPager.setAdapter(userProfileAdapter);

                PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
                tabs.setViewPager(viewPager);
                pb.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                System.out.println();
                pb.setVisibility(View.INVISIBLE);
            }
        }, screenName);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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

    private void populateUserInfo(User user) {

        TextView tvUser = (TextView) findViewById(R.id.tvUser);
        TextView tvScName = (TextView) findViewById(R.id.tvScName);
        TextView tvDesc = (TextView) findViewById(R.id.tvDescription);


        tvUser.setText(user.getName());
        tvScName.setText(user.getScreenName());
        tvDesc.setText(user.getDescription());

        ImageView ivUserPic = (ImageView) findViewById(R.id.ivUserPic);
        ivUserPic.setImageResource(android.R.color.transparent);
        Picasso.with(this).load(user.getProfilePicURL()).resize(100, 100).into(ivUserPic);

        ImageView userHeader = (ImageView) findViewById(R.id.rlUserHeader);
        if(user.getProfile_background_image_url()==null){
userHeader.setVisibility(View.GONE);
        }

        else {
            userHeader.setVisibility(View.VISIBLE);
            Picasso.with(this).load(user.getProfile_background_image_url()).into(userHeader);
        }
    }

    public class UserProfilePagerAdapter extends SmartFragmentStatePagerAdapter {


        final int PAGE_COUNT = 3;
        private String[] tabTitles = {"Tweets", "Following", "Followers"};

        public UserProfilePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
                Bundle bundle = new Bundle();
                bundle.putString("screenName", user.getScreenName());
                userTimelineFragment.setArguments(bundle);
                return userTimelineFragment;
            } else if (position == 1) {
                FollowingFragment followingFragment = new FollowingFragment();
                Bundle bundle = new Bundle();
                bundle.putString("screenName", user.getScreenName());
                followingFragment.setArguments(bundle);
                return followingFragment;
            } else {
                FollowersFragment followersFragment = new FollowersFragment();
                Bundle bundle = new Bundle();
                bundle.putString("screenName", user.getScreenName());
                followersFragment.setArguments(bundle);
                return followersFragment;
            }


        }

        @Override
        public int getCount() {
            return PAGE_COUNT;

        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return user.getTweetsCount() + " TWEETS";
                case 1:
                    return user.getFollowingCount() + " FOLLOWING";
                case 2:
                    return user.getFollowerCount() + " FOLLOWERS";
            }
            return tabTitles[position];
        }
    }


    public void launchProfile(String screenName) {


        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("screenName", screenName);
        startActivity(i);

    }

    public void reTweet(Tweet t) {

        ((TweetsListFragment) userProfileAdapter.getRegisteredFragment(0)).reTweet(t);

    }

    public void favorite(Tweet t, int position) {

        ((TweetsListFragment) userProfileAdapter.getRegisteredFragment(0)).favorite(t, position);

    }

    public void unFavorite(Tweet t, int position) {

        ((TweetsListFragment) userProfileAdapter.getRegisteredFragment(0)).unFavorite(t, position);

    }

    public void replyToTweet(Tweet tweet) {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        TweetFragment tweetDialog = TweetFragment.newInstance("Tweet");
        Bundle bundle = new Bundle();
        bundle.putBoolean("isReply", true);
        bundle.putSerializable("tweet", tweet);
        tweetDialog.setArguments(bundle);
        tweetDialog.show(fm, "activity_tweet");
    }

    @Override
    public void onTweetSaved(Tweet tweet) {

        viewPager.setCurrentItem(0);

        ((TweetsListFragment) userProfileAdapter.getRegisteredFragment(0)).updateTweetsView(tweet);


    }


}
