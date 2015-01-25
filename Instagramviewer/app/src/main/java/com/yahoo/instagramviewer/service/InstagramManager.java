package com.yahoo.instagramviewer.service;

import android.widget.ArrayAdapter;

import com.yahoo.instagramviewer.PhotoEntries;

import java.util.ArrayList;

/**
 * Created by nandaja on 1/23/15.
 */
public interface InstagramManager {

    public ArrayList<PhotoEntries> fetchPopularPhotos(ArrayAdapter<PhotoEntries> adapter);

}
