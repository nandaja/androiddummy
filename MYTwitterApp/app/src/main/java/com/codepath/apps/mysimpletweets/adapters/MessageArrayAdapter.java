package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Message;
import com.codepath.apps.mysimpletweets.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nandaja on 2/7/15.
 */
public class MessageArrayAdapter extends ArrayAdapter<Message> {


    public MessageArrayAdapter(Context context, List<Message> tweets){

        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Message message = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);
        }
        ImageView ivUserProfilePic = (ImageView)convertView.findViewById(R.id.ivUserProfilePic);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvTweetBody = (TextView) convertView.findViewById(R.id.tvTweetBody);
        TextView tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
        TextView tvTimeStamp = (TextView) convertView.findViewById(R.id.tvTimeStamp);
        ImageView isSent = (ImageView) convertView.findViewById(R.id.replyIcon);

        //Check if message is sent from authenticating user / received

        User user = null;

        if(message.isSent()){
            user = message.getRecipient();
            isSent.setVisibility(View.VISIBLE);

        }else{
            user = message.getSender();
            isSent.setVisibility(View.INVISIBLE);
        }
        tvUserName.setText(user.getName());
        tvTweetBody.setText(message.getDescription());
        tvScreenName.setText(user.getScreenName());

        tvTimeStamp.setText(message.getCreatedTime());
        ivUserProfilePic.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(user.getProfilePicURL()).resize(50,50).into(ivUserProfilePic);

        return convertView;
    }

}
