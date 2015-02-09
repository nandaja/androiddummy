package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.activities.TimelineActivity;
import com.codepath.apps.mysimpletweets.activities.TweetFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by nandaja on 2/7/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {


    public TweetsArrayAdapter(Context context, List<Tweet> tweets){

        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tweet tweet = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        ImageView ivUserProfilePic = (ImageView)convertView.findViewById(R.id.ivUserProfilePic);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvTweetBody = (TextView) convertView.findViewById(R.id.tvTweetBody);
        TextView tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
        TextView tvRetweetCount = (TextView) convertView.findViewById(R.id.tvRetweetCount);
        TextView tvFavCount = (TextView) convertView.findViewById(R.id.tvFavCount);
        TextView tvTimeStamp = (TextView) convertView.findViewById(R.id.tvTimeStamp);
        TextView tvRetweeted = (TextView) convertView.findViewById(R.id.tvRetweeted);
        ImageView ivRetweet = (ImageView) convertView.findViewById(R.id.reTweeted);

        tvUserName.setText(tweet.getUser().getName());
        tvTweetBody.setText(tweet.getBody());
        tvScreenName.setText(tweet.getUser().getScreenName());
        tvRetweetCount.setText(String.valueOf(tweet.getReTweetCount()));
        tvFavCount.setText(String.valueOf(tweet.getFavoriteCount()));
        tvTimeStamp.setText(tweet.getCreatedTime());
        ivUserProfilePic.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfilePicURL()).resize(50,50).into(ivUserProfilePic);


        if(tweet.getRetweeted()==null){
            tvRetweeted.setVisibility(View.GONE);
            ivRetweet.setVisibility(View.GONE);
        }

        else{
            tvRetweeted.setVisibility(View.VISIBLE);
            ivRetweet.setVisibility(View.VISIBLE);
            tvRetweeted.setText(tweet.getRetweeted());

        }
        ImageButton replyButton = (ImageButton) convertView.findViewById(R.id.replyIcon);
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fm = ((TimelineActivity) getContext()).fetchFragmentManager();
                TweetFragment tweetDialog = TweetFragment.newInstance("Tweet");
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", ((TimelineActivity) getContext()).getLoggedInUser());
                bundle.putBoolean("isReply", true);
                bundle.putSerializable("tweet", tweet);
                tweetDialog.setArguments(bundle);
                tweetDialog.show(fm, "activity_tweet");

            }
        });


        ImageButton reTweetButton = (ImageButton) convertView.findViewById(R.id.reTweetIcon);
        reTweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TimelineActivity)getContext()).reTweet(tweet);


            }
        });

        return convertView;
    }
}
