package com.codepath.apps.mysimpletweets.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends ActionBarActivity implements TweetCallBack {

    private TwitterClient client;
    private TweetsArrayAdapter tweetAdapter;
    private ArrayList<Tweet> tweets;
    private ListView lvTweets;
    private User user;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4099FF")));
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        setContentView(R.layout.activity_timeline);
        client = TwitterApplication.getRestClient();
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        tweets = new ArrayList<Tweet>();
        tweetAdapter = new TweetsArrayAdapter(this, tweets);
        lvTweets.setAdapter(tweetAdapter);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                // customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
                if (isNetworkAvailable()) {
                    System.out.println("NANDAJA :  offset is " + totalItemsCount + "page is " + page);

                    if (totalItemsCount >= 100) {
                        System.out.println("NANDAJA : Returning");
                        return;
                    }
                    populateTimeLine(totalItemsCount, false);
                } else {
                    getPersistedTweets();
                }
            }
        });


        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TimelineActivity.this, DetailedTweetActivity.class);
                intent.putExtra("user", user);
                Tweet tweet = tweets.get(position);
                intent.putExtra("tweet", tweet);
                startActivityForResult(intent, 100);


            }
        });

        if (!isNetworkAvailable()) {
            tweets.clear();
            tweetAdapter.notifyDataSetChanged();
            //Query SQLLite and fetch tweets to populate on the listView
            getPersistedTweets();
            Log.d("DEBUG", "displaying persisted tweets");
        } else {
            populateTimeLine(0, true);
            populateUserInfo();
            swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
            // Setup refresh listener which triggers new data loading
            swipeContainer.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Your code to refresh the list here.
                    // Make sure you call swipeContainer.setRefreshing(false)
                    // once the network request has completed successfully.
                    populateTimeLine(0, true);
                }
            });
            // Configure the refreshing colors
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

        }


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

    //Send an API request and fill the list view by creating tweet objects
    private void populateTimeLine(final int totalItems, final boolean clear) {

        long maxId = 0;
        long sinceId = 0;

        if (clear) {
            tweetAdapter.clear();
        }


        if (totalItems != 0) {

            maxId = Tweet.getMaxId(tweets);
            sinceId = Tweet.getSinceId(tweets);

        }
        Log.d("DEBUG", "Since id and max id are : " + sinceId + " " + maxId);
        client.getHomeTimeLine(maxId, sinceId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] header, JSONArray response) {

                Log.d("DEBUG", "Retrived tweets");
                //De-serialize JSON to create model and load model data into the list view
                tweets.addAll(Tweet.fromJSONArray(response));
                tweetAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                Log.d("DEBUG", "Failed to retrieve tweets" + errorResponse);
            }
        });
    }

    private void postTweet() {

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        TweetFragment tweetDialog = TweetFragment.newInstance("Tweet");
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        tweetDialog.setArguments(bundle);
        tweetDialog.show(fm, "activity_tweet");

    }

    @Override
    public void onTweetSaved(Tweet tweet) {

        tweetAdapter.insert(tweet, 0);

    }

    //Send an API request and fill the user information
    private void populateUserInfo() {

        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] header, JSONObject response) {

                user = new User();
                try {
                    user.setScreenName(response.getString("screen_name"));
                    user.setName(response.getString("name"));
                    user.setProfilePicURL(response.getString("profile_image_url"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void getPersistedTweets() {

        List<Tweet> tweetList = null;

        long leastId = 0l;


        leastId = Tweet.getMaxId(tweets);

        int count = new Select().from(Tweet.class).execute().size();
        Log.d("DEBUG", "COUNT is " + count);
        if (leastId == 0l) {
            tweetList = new Select()
                    .from(Tweet.class)
                    .limit(25).orderBy("tweetId DESC").execute();
            Log.d("DEBUG", "Tweet list size" + tweetList.size());
        } else {
            tweetList = new Select()
                    .from(Tweet.class)
                    .limit(25)
                    .where("tweetId < ?", leastId).execute();
            Log.d("DEBUG", "Max id : " + leastId + "Tweet list size" + tweetList.size());
        }

        tweets.addAll(tweetList);
        tweetAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode == 100 && requestCode==100) {
           Tweet t = (Tweet) data.getSerializableExtra("tweet");
            tweetAdapter.insert(t, 0);
        }
    }


    public FragmentManager fetchFragmentManager() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager;
    }

    public User getLoggedInUser(){
        return user;
    }

}
