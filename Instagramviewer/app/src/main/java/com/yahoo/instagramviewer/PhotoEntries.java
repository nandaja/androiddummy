package com.yahoo.instagramviewer;

import android.text.format.DateUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by nandaja on 1/23/15.
 */
public class PhotoEntries {

    // Graphic, Caption, Username
    //      (Optional) relative timestamp, like count, user profile image

    private String url;
    private String caption;
    private String username;
    private String date;
    private String likeCount;
    private String userProfilePic;


    private ArrayList<CommentEntries> comments;

    private String comment1;
    private String comment2;
    private String comment1User;


    private String comment2User;


    private String commentCount;

    public static String getDateTimeISO(Date aDate) {
        return String.format("%tFT%<tTZ", aDate);
    }

    public static Date getDateFromUtc(long timestamp) {
        return new Date(timestamp * 1000);
    }

    public String getComment1User() {
        return comment1User;
    }

    public void setComment1User(String comment1User) {
        this.comment1User = comment1User;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String timeInUtc) {
        this.date = DateUtils.getRelativeTimeSpanString(Long.parseLong(timeInUtc) * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    public String getComment1() {
        return comment1;
    }

    public void setComment1(String comment1) {
        this.comment1 = comment1;
    }

    public String getComment2() {
        return comment2;
    }

    public void setComment2(String comment2) {
        this.comment2 = comment2;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getComment2User() {
        return comment2User;
    }

    public void setComment2User(String comment2User) {
        this.comment2User = comment2User;
    }

    public ArrayList<CommentEntries> getComments() {
        return comments;
    }

    public void setComments(ArrayList<CommentEntries> comments) {
        this.comments = comments;
    }

}
