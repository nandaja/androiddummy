package com.yahoo.instagramviewer.datamodel;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by nandaja on 1/23/15.
 */
public class Likes {
    private String count;

    @JsonProperty("data")
    private LikesData[] data;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public LikesData[] getData() {
        return data;
    }

    public void setData(LikesData[] data) {
        this.data = data;
    }
}