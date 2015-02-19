package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by nandaja on 1/30/15.
 */
public class UserListAdapter extends ArrayAdapter<User> {

    public UserListAdapter(Context context, ArrayList<User> imageResults) {
        super(context, R.layout.item_user, imageResults);
    }

    @Override
    public View getView ( int position, View convertView, ViewGroup parent){

        User user = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user,parent,false);
        }

        TextView tvUser = (TextView) convertView.findViewById(R.id.tvUser);
        TextView tvScName = (TextView) convertView.findViewById(R.id.tvScName);
        TextView tvDesc = (TextView) convertView.findViewById(R.id.tvDescription);


        tvUser.setText(user.getName());
        tvScName.setText(user.getScreenName());
        tvDesc.setText(user.getDescription());

        ImageView ivUserPic = (ImageView) convertView.findViewById(R.id.ivUserImage);
        ivUserPic.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(user.getProfilePicURL()).resize(80, 80).into(ivUserPic);

        return convertView;

    }



}
