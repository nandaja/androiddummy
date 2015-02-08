package com.codepath.apps.mysimpletweets.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity implements TweetCallBack {

    private TwitterClient client;
    private TweetsArrayAdapter tweetAdapter;
    private ArrayList<Tweet> tweets;
    private ListView lvTweets;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4099FF")));
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);

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
                System.out.println("NANDAJA :  offset is " + totalItemsCount + "page is " + page);

                if (totalItemsCount >= 75) {
                    System.out.println("NANDAJA : Returning");
                    return;
                }
                populateTimeLine(totalItemsCount, false);
            }
        });

        populateTimeLine(0, true);
        populateUserInfo();

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

        tweetAdapter.insert(tweet,0);
    }

    //Send an API request and fill the user information
    private void populateUserInfo() {

        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] header, JSONObject response) {

                user = new User();
                try{
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
}
