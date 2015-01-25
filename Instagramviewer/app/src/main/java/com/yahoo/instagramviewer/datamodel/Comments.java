package com.yahoo.instagramviewer.datamodel;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class Comments {
    private Number count;
    @JsonProperty("data")
    private List<CommentData> data;

    public Number getCount() {
        return this.count;
    }

    public void setCount(Number count) {
        this.count = count;
    }

    public List<CommentData> getData() {
        return this.data;
    }

    public void setData(List<CommentData> data) {
        this.data = data;
    }
}
