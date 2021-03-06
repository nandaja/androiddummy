package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import java.text.MessageFormat;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "qA7mTbnzMtFI5JF3jozYiJVl4";       // Change this
    public static final String REST_CONSUMER_SECRET = "MM29ycZ7kITwdg5bWf7GIBJRMnqDIEGIfWEGhXWdCrCjJLF2Yw"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://simpletweets"; // Change this (here and in manifest)

    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    //METHOD == ENDPOINT
    //GET Home Timeline API

    /**
     * GET https://api.twitter.com/1.1/statuses/home_timeline.json
     * Provide count=25, since_id=1
     * count , since_id , max_id ( for pagination )
     */

    public void getHomeTimeLine(long maxid, long sinceid, AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();

        //First call
        if(sinceid==0) {
            params.put("count", 50);
        }
        else{
            params.put("count", 50);
            Log.d("DEBUG", "since id : " + sinceid + " maxId : " + maxid);
            params.put("max_id", maxid - 1);
            params.put("since_id", sinceid);
        }

        client.get(apiURL, params, handler);
        Log.d("DEBUG", "TEST");
    }

    /**
     * https://api.twitter.com/1.1/account/verify_credentials.json
     *
     */

    public void getUserInfo(AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("account/verify_credentials.json");
        RequestParams params = new RequestParams();
        client.get(apiURL, params, handler);
        Log.d("DEBUG", "TEST");
    }

    /***
     * statuses/update.json
     */

    public void postTweet(String tweet, long tweetIdInReplyTo, AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", tweet);

        if(tweetIdInReplyTo != 0l){
            params.put("in_reply_to_status_id", tweetIdInReplyTo);
        }
        client.post(apiURL, params, handler);
        Log.d("DEBUG", "TEST");
    }

    /***
     * https://api.twitter.com/1.1/statuses/retweet/:id.json
     */

    public void reTweet(long id, AsyncHttpResponseHandler handler) {

        String url = MessageFormat.format("statuses/retweet/{0}.json", id);
        url = url.replace(",", "");

        String apiURL = getApiUrl(url);
        Log.d("DEBUG", "retweet url " + url);

        RequestParams params = new RequestParams();
        client.post(apiURL, params, handler);
        Log.d("DEBUG", "Retweeted " + id);
    }

    /**
     * GET https://api.twitter.com/1.1/statuses/mentions_timeline.json
     * Provide count=25, since_id=1
     * count , since_id , max_id ( for pagination )
     */

    public void getMentionsTimeLine(long maxid, long sinceid, AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();

        //First call
        if(sinceid==0) {
            params.put("count", 50);
        }
        else{
            params.put("count", 50);
            Log.d("DEBUG", "since id : " + sinceid + " maxId : " + maxid);
            params.put("max_id", maxid - 1);
            params.put("since_id", sinceid);
        }

        client.get(apiURL, params, handler);
        Log.d("DEBUG", "TEST");
    }

    /**
     * Mark tweet as favorite : favorites/create.json
     * param : id
     */

    public void favorite(long id, AsyncHttpResponseHandler handler) {

        String url = "favorites/create.json";
        url = url.replace(",", "");

        String apiURL = getApiUrl(url);
        Log.d("DEBUG", "Favorite url " + url);

        RequestParams params = new RequestParams();
        params.put("id", id);
        client.post(apiURL, params, handler);
        Log.d("DEBUG", "Favorited " + id);
    }
    /**
     * Unfavorite : favorites/destroy.json
     * param : id
     */

    public void unFavorite(long id, AsyncHttpResponseHandler handler) {

        String url = "favorites/destroy.json";
        url = url.replace(",", "");

        String apiURL = getApiUrl(url);
        Log.d("DEBUG", "Unfavorite url " + url);

        RequestParams params = new RequestParams();
        params.put("id", id);
        client.post(apiURL, params, handler);

        Log.d("DEBUG", "Unfavorited " + id);
    }

    public void getUserTimeline(long maxid, long sinceid, AsyncHttpResponseHandler handler, String screenName) {
        String apiURL = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();

        //First call
        if(sinceid==0) {
            params.put("count", 50);
        }
        else{
            params.put("count", 50);
            Log.d("DEBUG", "since id : " + sinceid + " maxId : " + maxid);
            params.put("max_id", maxid - 1);
            params.put("since_id", sinceid);
        }

        if(screenName !=null){
            params.put("screen_name", screenName);
        }

        client.get(apiURL, params, handler);
        Log.d("DEBUG", "TEST");
    }

   // users/show.json

    public void getUser( AsyncHttpResponseHandler handler, String screenName) {
        String apiURL = getApiUrl("users/show.json");
        RequestParams params = new RequestParams();

        if(screenName !=null){
            params.put("screen_name", screenName);
        }

        client.get(apiURL, params, handler);
        Log.d("DEBUG", "TEST");
    }

    //friends/list.json

    public void getFollowings( AsyncHttpResponseHandler handler, String screenName) {
        String apiURL = getApiUrl("friends/list.json");
        RequestParams params = new RequestParams();

        params.put("screen_name", screenName.replace("@", ""));
        params.put("count", 200);


        client.get(apiURL, params, handler);
        Log.d("DEBUG", "TEST");
    }

    //followers/list.json

    public void getFollowers( AsyncHttpResponseHandler handler, String screenName) {
        String apiURL = getApiUrl("followers/list.json");
        RequestParams params = new RequestParams();

        params.put("screen_name", screenName.replace("@", ""));
        params.put("count", 200);


        client.get(apiURL, params, handler);
        Log.d("DEBUG", "TEST");
    }

    /**
     * "search/tweets.json"
     * @param maxid
     * @param sinceid
     * @param tweetString
     * @param handler
     */
    public void getSearchTweetResults(long maxid, long sinceid, String tweetString, AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("search/tweets.json");
        RequestParams params = new RequestParams();

        params.put("q", tweetString);
        //First call
        if(sinceid==0) {
            params.put("count", 50);
        }
        else{
            params.put("count", 50);
            Log.d("DEBUG", "since id : " + sinceid + " maxId : " + maxid);
            params.put("max_id", maxid - 1);
            params.put("since_id", sinceid);
        }

        client.get(apiURL, params, handler);
        Log.d("DEBUG", "TEST");
    }

    public void getMessages(long maxid, long sinceid, AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("direct_messages.json");
        RequestParams params = new RequestParams();

        //First call
        if(sinceid==0) {
            params.put("count", 50);
        }
        else{
            params.put("count", 50);
            Log.d("DEBUG", "since id : " + sinceid + " maxId : " + maxid);
            params.put("max_id", maxid - 1);
            params.put("since_id", sinceid);
        }

        client.get(apiURL, params, handler);
        Log.d("DEBUG", "TEST");
    }


}