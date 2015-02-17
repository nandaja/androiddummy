package com.codepath.apps.mysimpletweets.models;

/**
 * Created by nandaja on 2/7/15.
 */

import android.text.format.DateUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/***
 *

 [
 {
 "coordinates": null,
 "truncated": false,
 "created_at": "Tue Aug 28 21:16:23 +0000 2012",
 "favorited": false,
 "id_str": "240558470661799936",
 "in_reply_to_user_id_str": null,
 "entities": {
 "urls": [

 ],
 "hashtags": [

 ],
 "user_mentions": [

 ]
 },
 "text": "just another test",
 "contributors": null,
 "id": 240558470661799936,
 "retweet_count": 0,
 "in_reply_to_status_id_str": null,
 "geo": null,
 "retweeted": false,
 "in_reply_to_user_id": null,
 "place": null,
 "source": "<a href="//realitytechnicians.com\"" rel="\"nofollow\"">OAuth Dancer Reborn</a>",
 "user": {
 "name": "OAuth Dancer",
 "profile_sidebar_fill_color": "DDEEF6",
 "profile_background_tile": true,
 "profile_sidebar_border_color": "C0DEED",
 "profile_image_url":"http://a0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
 "created_at": "Wed Mar 03 19:37:35 +0000 2010",
 "location": "San Francisco, CA",
 "follow_request_sent": false,
 "id_str": "119476949",
 "is_translator": false,
 "profile_link_color": "0084B4",
 "entities": {
 "url": {
 "urls": [
 {
 "expanded_url": null,
 "url": "http://bit.ly/oauth-dancer",
 "indices": [
 0,
 26
 ],
 "display_url": null
 }
 ]
 },
 "description": null
 },
 "default_profile": false,
 "url": "http://bit.ly/oauth-dancer",
 "contributors_enabled": false,
 "favourites_count": 7,
 "utc_offset": null,
 "profile_image_url_https":"https://si0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
 "id": 119476949,
 "listed_count": 1,
 "profile_use_background_image": true,
 "profile_text_color": "333333",
 "followers_count": 28,
 "lang": "en",
 "protected": false,
 "geo_enabled": true,
 "notifications": false,
 "description": "",
 "profile_background_color": "C0DEED",
 "verified": false,
 "time_zone": null,
 "profile_background_image_url_https":"https://si0.twimg.com/profile_background_images/80151733/oauth-dance.png",
 "statuses_count": 166,
 "profile_background_image_url":"http://a0.twimg.com/profile_background_images/80151733/oauth-dance.png",
 "default_profile_image": false,
 "friends_count": 14,
 "following": false,
 "show_all_inline_media": false,
 "screen_name": "oauth_dancer"
 },
 "in_reply_to_screen_name": null,
 "in_reply_to_status_id": null
 },

 ]

 */
@Table(name = "Tweet")
public class Tweet  extends Model implements Serializable {

    final String TWITTER="EEE MMM dd HH:mm:ss ZZZZZ yyyy";

    @Column(name = "body")
    private String body;

    @Column(name = "tweetId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long tweetId;

    @Column(name = "User", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private User user;

    @Column(name = "createdTime")
    private String createdTime;

    @Column(name = "reTweetCount")
    private int reTweetCount;//retweet_count

    @Column(name = "favoriteCount")
    private int favoriteCount;//favorite_count

    @Column(name = "embeddedImageURL")
    private String embeddedImageURL;

    @Column(name = "favorited")
    private boolean favorited;

    private String retweeted;


    public String getRetweeted() {
        return retweeted;
    }

    public void setRetweeted(String retweeted) {
        this.retweeted = retweeted;
    }

    public String getEmbeddedImageURL() {
        return embeddedImageURL;
    }

    public void setEmbeddedImageURL(String embeddedImageURL) {
        this.embeddedImageURL = embeddedImageURL;
    }



    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getReTweetCount() {
        return reTweetCount;
    }

    public void setReTweetCount(int reTweetCount) {
        this.reTweetCount = reTweetCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getTweetId() {
        return tweetId;
    }

    public void setTweetId(long tweetId) {
        this.tweetId = tweetId;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String c) {

        try {
            SimpleDateFormat sf = new SimpleDateFormat(TWITTER);
            sf.setLenient(true);
            Date date = sf.parse(c);
            this.createdTime = DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            if(createdTime.contains("seconds ago"))
                this.createdTime = this.createdTime.replace("seconds ago", "s");
            else if(createdTime.contains("minutes ago"))
                this.createdTime = this.createdTime.replace("minutes ago", "m");
            else if(createdTime.contains("hours ago"))
                this.createdTime = this.createdTime.replace("hours ago", "h");
            else if(createdTime.contains("hour ago"))
                this.createdTime = this.createdTime.replace("hour ago", "h");

        }
        catch(ParseException e){

        }
    }


    public static Tweet buildTweet(JSONObject jsonResponse){

        Tweet tweet = new Tweet();
        try {
            if(jsonResponse.has("retweeted_status") == false){
            tweet.setBody(jsonResponse.getString("text"));
            tweet.setCreatedTime(jsonResponse.getString("created_at"));
            tweet.setTweetId(jsonResponse.getLong("id"));
            tweet.setUser(User.buildUser(jsonResponse.getJSONObject("user")));
            tweet.setReTweetCount(jsonResponse.getInt("retweet_count"));
            tweet.setFavoriteCount(jsonResponse.getInt("favorite_count"));
            tweet.setFavorited(jsonResponse.getBoolean("favorited"));

        }
            else{

                //Get retweeted status and populate the tweet

                User retweetingUser = User.buildUser(jsonResponse.getJSONObject("user"));
                JSONObject retweetedStatus = jsonResponse.getJSONObject("retweeted_status");
                tweet.setBody(retweetedStatus.getString("text"));
                tweet.setCreatedTime(jsonResponse.getString("created_at"));
                tweet.setTweetId(retweetedStatus.getLong("id"));
                tweet.setUser(User.buildUser(retweetedStatus.getJSONObject("user")));
                tweet.setReTweetCount(retweetedStatus.getInt("retweet_count"));
                tweet.setFavoriteCount(retweetedStatus.getInt("favorite_count"));
                tweet.setFavorited(jsonResponse.getBoolean("favorited"));
                tweet.retweeted = retweetingUser.getName() + " retweeted";

            }

            try {
                //Read media if any and extract image URL
                JSONObject entity = jsonResponse.getJSONObject("entities");
                if (entity != null) {
                    JSONArray media = entity.getJSONArray("media");
                    if (media != null) {
                        JSONObject image = media.getJSONObject(0);
                        if (image != null) {
                            tweet.setEmbeddedImageURL(image.getString("media_url"));
                        }

                    }
                }
            }
            catch(JSONException e){

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray array){

        ArrayList<Tweet> tweetList = new ArrayList<Tweet>();
        for(int i =0; i<array.length(); i++){

            try {
                Tweet tweet = buildTweet(array.getJSONObject(i));
                tweet.save();
                if(tweet!=null) {
                    tweetList.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

        }

        return tweetList;
    }

    public static long getMaxId(ArrayList<Tweet> tweetArray){

        if(tweetArray!=null && tweetArray.size()!=0) {
            long maxId = tweetArray.get(0).getTweetId();

            for (Tweet t : tweetArray) {
                if (t.getTweetId() <= maxId)
                    maxId = t.getTweetId();
            }
            return maxId;
        }
        else
        return 0l;
    }

    public static long getSinceId(ArrayList<Tweet> tweetArray){
        long sinceId = tweetArray.get(0).getTweetId();

        for(Tweet t:tweetArray){
            if(t.getTweetId()>=sinceId)
                sinceId = t.getTweetId();
        }
        return sinceId;

    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }


}
