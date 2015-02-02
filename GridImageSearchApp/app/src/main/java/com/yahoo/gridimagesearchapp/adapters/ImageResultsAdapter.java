package com.yahoo.gridimagesearchapp.adapters;

import android.content.Context;
import android.graphics.Movie;
import android.media.Image;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yahoo.gridimagesearchapp.R;
import com.yahoo.gridimagesearchapp.models.ImageResult;

import java.util.ArrayList;

/**
 * Created by nandaja on 1/30/15.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

    public ImageResultsAdapter(Context context, ArrayList<ImageResult> imageResults) {
        super(context, R.layout.image_item, imageResults);
    }

    @Override
    public View getView ( int position, View convertView, ViewGroup parent){

        ImageResult image = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.image_item,parent,false);
        }
        ImageView ivImage = (ImageView)convertView.findViewById(R.id.ivImage);
       // TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        ivImage.setImageResource(0);
     //   tvTitle.setText(Html.fromHtml(image.getTitle()));
        Picasso.with(getContext()).load(image.getThumbUrl()).into(ivImage);
        return convertView;

    }



}
