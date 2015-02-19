package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.adapters.MessageArrayAdapter;
import com.codepath.apps.mysimpletweets.models.Message;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nandaja on 2/16/15.
 */
public class MessagesFragment extends Fragment {


    private String screenName;
    private ListView lvMessages;
    private TwitterClient client;
    private ArrayList<Message> messages;
    private MessageArrayAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        //All view related stuff goes in here

        super.onCreateView(inflater, container, savedInstanceState);
        client  = TwitterApplication.getRestClient();

        //Create your view
        View view = inflater.inflate(R.layout.fragment_messages_list, container, false);

        lvMessages = (ListView) view.findViewById(R.id.lvMessages);
        screenName = this.getArguments().getString("screenName");

        //populateList

        messages = new ArrayList<Message>();
        mAdapter = new MessageArrayAdapter(this.getActivity(), messages);
        lvMessages.setAdapter(mAdapter);

        populateList(screenName);
        return view;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {

        //Initialize data that are not view related
        super.onCreate(savedInstanceState);



    }


    //Send an API request and fill the list view by creating tweet objects
    private void populateList(final String screenName) {

        client.getMessages(0, 0, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] header, JSONArray response) {

                Log.d("DEBUG", "Retrived messages");
                //De-serialize JSON to create model and load model data into the list view
                try {

                    for (int i = 0; i < response.length(); i++) {
                        Message m = Message.buildMessage(response.getJSONObject(i));
                        if(m.getSenderScreenName().equals(screenName)){
                            m.setSent(true);
                        }
                        mAdapter.add(m);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                Log.d("DEBUG", "Failed to retrieve messages" + errorResponse);
            }
        });
    }

}



