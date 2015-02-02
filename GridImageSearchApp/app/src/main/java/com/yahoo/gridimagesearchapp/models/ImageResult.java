package com.yahoo.gridimagesearchapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nandaja on 1/30/15.
 */
public class ImageResult implements Serializable{

    private String fullURL;
    private String thumbUrl;
    private String title;
    private int width;
    private int height;

    public ImageResult(JSONObject json) {

        try {

            this.fullURL = json.getString("url");
            this.thumbUrl = json.getString("tbUrl");
            this.title = json.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getFullURL() {
        return fullURL;
    }

    public void setFullURL(String fullURL) {
        this.fullURL = fullURL;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    public static ArrayList<ImageResult> fromJson(JSONArray results) {

        ArrayList<ImageResult> list = new ArrayList<ImageResult>();
        for (int i = 0; i < results.length(); i++) {
            try {
                list.add(new ImageResult(results.getJSONObject(i)));
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
        return list;
    }

}
