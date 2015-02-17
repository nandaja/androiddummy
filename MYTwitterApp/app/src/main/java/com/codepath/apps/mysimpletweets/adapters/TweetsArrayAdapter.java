package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.activities.TimelineActivity;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nandaja on 2/7/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {


    public TweetsArrayAdapter(Context context, List<Tweet> tweets){

        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        ivUserProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TimelineActivity) getContext()).launchProfile(tweet.getUser().getScreenName());
            }
        });


        if(tweet.getRetweeted()==null){
            tvRetweeted.setVisibility(View.GONE);
            ivRetweet.setVisibility(View.GONE);
        }

        else{
            tvRetweeted.setVisibility(View.VISIBLE);
            ivRetweet.setVisibility(View.VISIBLE);
            tvRetweeted.setText(tweet.getRetweeted());

        }

        ImageButton ivFavorite = (ImageButton) convertView.findViewById(R.id.favoritesIcon);
        if(tweet.isFavorited()){
            ivFavorite.setBackgroundResource(R.drawable.ic_star_filled);
            tvFavCount.setTextColor(Color.parseColor("#ffa500"));

        }
        else{
            tvFavCount.setTextColor(Color.parseColor("#000000"));
            ivFavorite.setBackgroundResource(R.drawable.ic_star_unfilled);
        }
        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!tweet.isFavorited())
                ((TimelineActivity) getContext()).favorite(tweet, position);
                else
                    ((TimelineActivity) getContext()).unFavorite(tweet, position);
            }
        });

        ImageButton replyButton = (ImageButton) convertView.findViewById(R.id.replyIcon);
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TimelineActivity) getContext()).replyToTweet(tweet);
            }
        });


        ImageButton reTweetButton = (ImageButton) convertView.findViewById(R.id.reTweetIcon);
        reTweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((TimelineActivity) getContext()).reTweet(tweet);
            }
        });



        return convertView;
    }

}
