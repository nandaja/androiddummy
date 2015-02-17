package com.codepath.apps.mysimpletweets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.activities.DetailedTweetActivity;
import com.codepath.apps.mysimpletweets.activities.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nandaja on 2/16/15.
 */
public class MentionsFragment extends TweetsListFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        //All view related stuff goes in here

        super.onCreateView(inflater, container, savedInstanceState);

        //Create your view
        View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        lvTweets = (ListView) view.findViewById(R.id.lvTweets);
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
                Intent intent = new Intent(getActivity(), DetailedTweetActivity.class);
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

            swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
            // Setup refresh listener which triggers new data loading
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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

        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        //Initialize data that are not view related
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<Tweet>();
        tweetAdapter = new TweetsArrayAdapter(getActivity(), tweets);


    }

    public void addAll(ArrayList<Tweet> tweets){
        tweetAdapter.addAll(tweets);

        //De-serialize JSON to create model and load model data into the list view
        //
        //tweetAdapter.notifyDataSetChanged();
        //swipeContainer.setRefreshing(false);

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
        client.getMentionsTimeLine(maxId, sinceId, new JsonHttpResponseHandler() {
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

}
