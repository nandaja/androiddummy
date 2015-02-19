package com.codepath.apps.mysimpletweets.fragments;

/**
 * Created by nandaja on 2/16/15.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.activities.ProfileActivity;
import com.codepath.apps.mysimpletweets.adapters.UserListAdapter;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by nandaja on 2/16/15.
 */
public class FollowingFragment  extends Fragment  {

    private String screenName;

    private ListView gvResults;
    private ArrayList<User> users;
    private UserListAdapter userAdapter;
    TwitterClient client;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        //All view related stuff goes in here

        super.onCreateView(inflater, container, savedInstanceState);
        client  = TwitterApplication.getRestClient();

        //Create your view
        View view = inflater.inflate(R.layout.fragment_user_grid, container, false);

        screenName = this.getArguments().getString("screenName");

        //populateList

        setUpViews(view);
        users = new ArrayList<User>();
        userAdapter = new UserListAdapter(this.getActivity(), users);
        gvResults.setAdapter(userAdapter);

        populateList(screenName);
        return view;
    }


    private void setUpViews(View view) {
        // etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (ListView) view.findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                User user = users.get(position);
                intent.putExtra("screenName", user.getScreenName());
                startActivity(intent);
            }
        });

    /*    gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                // customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);

                System.out.println("NANDAJA :  offset is " + totalItemsCount + "page is " + page);
                if(totalItemsCount >= 64) {
                    return;
                }
                getImageData(totalItemsCount, false);
            }
        });

*/


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        //Initialize data that are not view related
        super.onCreate(savedInstanceState);



    }


    //Send an API request and fill the list view by creating tweet objects
    private void populateList(String screenName) {

        client.getFollowings( new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] header, JSONObject response) {

                Log.d("DEBUG", "Retrived followings");
                //De-serialize JSON to create model and load model data into the list view
                try {
                    JSONArray userList = response.getJSONArray("users");
                    for(int i=0;i<userList.length();i++){
                    userAdapter.add(User.buildUser(userList.getJSONObject(i)));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                Log.d("DEBUG", "Failed to retrieve followings" + errorResponse);
            }
        }, screenName);
    }

}
