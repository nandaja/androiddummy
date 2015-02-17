package com.codepath.apps.mysimpletweets.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandaja on 2/15/15.
 */
public class TweetsListFragment  extends Fragment {

    TweetsArrayAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    ListView lvTweets;
    User user;
    SwipeRefreshLayout swipeContainer;
    TwitterClient client;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        //All view related stuff goes in here

        client = TwitterApplication.getRestClient();

        //Create your view
        View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        lvTweets = (ListView) view.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(tweetAdapter);


        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        //Initialize data that are not view related
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<Tweet>();
        tweetAdapter = new TweetsArrayAdapter(getActivity(), tweets);


    }

    public void addAll(ArrayList<Tweet> tweets) {
        tweetAdapter.addAll(tweets);

        //De-serialize JSON to create model and load model data into the list view
        //
        //tweetAdapter.notifyDataSetChanged();
        //swipeContainer.setRefreshing(false);

    }

    Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) (getActivity().getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    void getPersistedTweets() {

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


    public void reTweet(Tweet t) {
        client.reTweet(t.getTweetId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] header, JSONObject response) {
                Tweet t1 = Tweet.buildTweet(response);

                tweetAdapter.insert(t1, 0);

                lvTweets.setSelectionAfterHeaderView();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });

    }


    public void updateTweetsView(Tweet t) {
        tweetAdapter.insert(t, 0);
        lvTweets.setSelectionAfterHeaderView();
    }

    public void favorite(Tweet t, final int position) {

        System.out.println("NANDAJA ###########");
        client.favorite(t.getTweetId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] header, JSONObject response) {
                tweetAdapter.getItem(position).setFavorited(true);
                tweetAdapter.notifyDataSetChanged();
                Log.d("DEBUG", "Favorited tweet");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", "UNFavorited tweet");
            }

        });

}

    public void unFavorite(Tweet t, final int position){

        client.unFavorite(t.getTweetId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] header, JSONObject response) {
                tweetAdapter.getItem(position).setFavorited(false);
                tweetAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }

        });

    }

}
