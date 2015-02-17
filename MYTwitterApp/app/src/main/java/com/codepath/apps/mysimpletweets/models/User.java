package com.codepath.apps.mysimpletweets.models;

/**
 * Created by nandaja on 2/7/15.
 */

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/***

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
 }
 */
@Table(name = "User")
public class User extends Model implements Serializable{

    @Column(name = "name")
    private String name;

    @Column(name = "screenName")
    private String screenName;

    @Column(name = "profilePicURL")
    private String profilePicURL;

    @Column(name = "userId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long userId;

    @Column(name = "tweetsCount")
    private long tweetsCount;

    @Column(name = "followingCount")
    private long followingCount;

    @Column(name = "followerCount")
    private long followerCount;

    @Column(name = "coverPicURL")
    private String coverPicURL;

    @Column(name = "description")
    private String description;

    @Column(name = "profile_background_image_url")
    private String profile_background_image_url;

    public String getProfile_background_image_url() {
        return profile_background_image_url;
    }

    public void setProfile_background_image_url(String profile_background_image_url) {
        this.profile_background_image_url = profile_background_image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverPicURL() {
        return coverPicURL;
    }

    public void setCoverPicURL(String coverPicURL) {
        this.coverPicURL = coverPicURL;
    }

    public long getTweetsCount() {
        return tweetsCount;
    }

    public void setTweetsCount(long tweetsCount) {
        this.tweetsCount = tweetsCount;
    }

    public long getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(long followingCount) {
        this.followingCount = followingCount;
    }

    public long getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(long followerCount) {
        this.followerCount = followerCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long id) {
        this.userId = id;
    }

    public static User buildUser(JSONObject userObj){

        User user = new User();
        try {
            user.setUserId(userObj.getLong("id"));
            user.setName(userObj.getString("name"));
            user.setName(userObj.getString("screen_name"));
            user.setProfilePicURL(userObj.getString("profile_image_url"));
            user.setScreenName("@" + userObj.getString("screen_name"));
            user.setFollowerCount(userObj.getInt("followers_count"));
            user.setFollowingCount(userObj.getInt("friends_count"));
            user.setTweetsCount(userObj.getInt("statuses_count"));
            user.setDescription(userObj.getString("description"));
            user.setProfile_background_image_url(userObj.getString("profile_background_image_url"));
            user.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
}
