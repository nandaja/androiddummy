package com.yahoo.instagramviewer.datamodel;

public class Caption {
    private String created_time;
    private From from;
    private String id;
    private String text;

    public String getCreated_time() {
        return this.created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public From getFrom() {
        return this.from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
