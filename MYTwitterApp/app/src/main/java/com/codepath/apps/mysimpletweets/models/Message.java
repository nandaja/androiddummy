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


@Table(name = "Message")
public class Message  extends Model implements Serializable {

    final String TWITTER="EEE MMM dd HH:mm:ss ZZZZZ yyyy";


    private boolean isSent;

    @Column(name = "description")
    private String description;

    @Column(name = "messageId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long messageId;

    @Column(name = "Sender", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private User sender;


    @Column(name = "Recipient", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private User recipient;

    @Column(name = "createdTime")
    private String createdTime;


    @Column(name = "recipientScreenName")
    private String recipientScreenName;

    @Column(name = "senderScreenName")
    private String senderScreenName;


    public String getRecipientScreenName() {
        return recipientScreenName;
    }

    public void setRecipientScreenName(String recipientScreenName) {
        this.recipientScreenName = recipientScreenName;
    }

    public String getSenderScreenName() {
        return senderScreenName;
    }

    public void setSenderScreenName(String senderScreenName) {
        this.senderScreenName = senderScreenName;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean isSent) {
        this.isSent = isSent;
    }


    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
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


    public static Message buildMessage(JSONObject jsonResponse){

        Message message = new Message();
        try {
            message.setDescription(jsonResponse.getString("text"));
            User sender = User.buildUser(jsonResponse.getJSONObject("sender"));
            User recipient = User.buildUser(jsonResponse.getJSONObject("recipient"));
            message.setSender(sender);
            message.setRecipient(recipient);
            message.setSenderScreenName(jsonResponse.getString("sender_screen_name"));
            message.setRecipientScreenName(jsonResponse.getString("recipient_screen_name"));
            message.setCreatedTime(jsonResponse.getString("created_at"));
        }

            catch(JSONException e){

            }


        return message;
    }

    public static ArrayList<Message> fromJSONArray(JSONArray array){

        ArrayList<Message> tweetList = new ArrayList<Message>();
        for(int i =0; i<array.length(); i++){

            try {
                Message tweet = buildMessage(array.getJSONObject(i));
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




}
