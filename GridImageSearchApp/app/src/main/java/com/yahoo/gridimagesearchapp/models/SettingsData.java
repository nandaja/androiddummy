package com.yahoo.gridimagesearchapp.models;

import java.io.Serializable;

/**
 * Created by nandaja on 2/1/15.
 */
public class SettingsData implements Serializable {

    private String color;
    private String size;
    private String type;
    private String site;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
