package com.codepath.apps.mysimpletweets.activities;


import com.codepath.apps.mysimpletweets.models.Tweet;

/**
 * Created by nandaja on 2/2/15.
 */
public interface TweetCallBack {

    public void onTweetSaved(Tweet tweet);
}
