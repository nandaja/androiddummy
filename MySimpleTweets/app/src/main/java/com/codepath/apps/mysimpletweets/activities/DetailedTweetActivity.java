package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.squareup.picasso.Picasso;

public class DetailedTweetActivity extends ActionBarActivity implements TweetCallBack {

    private TwitterClient client;
    private User user;
    Tweet tweet;

    /**
     * "media":[{"type":"photo", "sizes":{"thumb":{"h":150, "resize":"crop", "w":150}, "large":{"h":238, "resize":"fit", "w":226}, "medium":{"h":238, "resize":"fit", "w":226}, "small":{"h":238, "resize":"fit", "w":226}}, "indices":[15,35], "url":"http:\/\/t.co\/rJC5Pxsu", "media_url":"http:\/\/p.twimg.com\/AZVLmp-CIAAbkyy.jpg", "display_url":"pic.twitter.com\/rJC5Pxsu", "id":114080493040967680, "id_str":"114080493040967680", "expanded_url": "http:\/\/twitter.com\/yunorno\/status\/114080493036773378\/photo\/1", "media_url_https":"https:\/\/p.twimg.com\/AZVLmp-CIAAbkyy.jpg"}]
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4099FF")));
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        setContentView(R.layout.activity_detailed_tweet);

        tweet = (Tweet) getIntent().getSerializableExtra("tweet");
        user = (User) getIntent().getSerializableExtra("user");

        ImageView ivUserProfilePic = (ImageView) findViewById(R.id.ivUserProfilePic);
        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        TextView tvTweetBody = (TextView) findViewById(R.id.tvTweetBody);
        TextView tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        TextView tvRetweetCount = (TextView) findViewById(R.id.tvRetweetCount);
        TextView tvFavCount = (TextView) findViewById(R.id.tvFavCount);
        TextView tvTimeStamp = (TextView) findViewById(R.id.tvTimeStamp);

        ImageView ivImage = (ImageView) findViewById(R.id.ivImage);

        tvUserName.setText(tweet.getUser().getName());
        tvTweetBody.setText(tweet.getBody());
        tvScreenName.setText(tweet.getUser().getScreenName());
        tvRetweetCount.setText(String.valueOf(tweet.getReTweetCount()));
        tvFavCount.setText(String.valueOf(tweet.getFavoriteCount()));
        tvTimeStamp.setText(tweet.getCreatedTime());
        ivUserProfilePic.setImageResource(android.R.color.transparent);
        Picasso.with(this).load(tweet.getUser().getProfilePicURL()).resize(50, 50).into(ivUserProfilePic);

        if (tweet.getEmbeddedImageURL() != null) {
            ivImage.setImageResource(android.R.color.transparent);
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    System.out.println(uri.toString());
                    exception.printStackTrace();
                }
            }).build();
            picasso.load(tweet.getEmbeddedImageURL()).into(ivImage);
            ivImage.setVisibility(View.VISIBLE);
        } else {
            ivImage.setImageResource(android.R.color.transparent);
            ivImage.setVisibility(View.GONE);

        }

        ImageButton replyButton = (ImageButton) findViewById(R.id.replyIcon);
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postTweet();

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detailed_tweet, menu);
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

    private void postTweet() {

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        TweetFragment tweetDialog = TweetFragment.newInstance("Tweet");
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        bundle.putBoolean("isReply", true);
        bundle.putSerializable("tweet", tweet);
        tweetDialog.setArguments(bundle);
        tweetDialog.show(fm, "activity_tweet");

    }

    @Override
    public void onTweetSaved(Tweet tweet) {
        Intent intent = new Intent();
        intent.putExtra("tweet", tweet);
        setResult(100, intent);
        finish();
    }


}
