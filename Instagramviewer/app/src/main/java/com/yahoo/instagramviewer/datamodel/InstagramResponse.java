package com.yahoo.instagramviewer.datamodel;

import java.util.List;

public class InstagramResponse {
    private List<Data> data;
    private Meta meta;

    public List<Data> getData() {
        return this.data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Meta getMeta() {
        return this.meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
