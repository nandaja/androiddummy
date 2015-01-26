package com.yahoo.instagramviewer;

/**
 * Created by nandaja on 1/23/15.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by nandaja on 1/20/15.
 */
public class CommentEntriesAdapter extends ArrayAdapter<CommentEntries> {

    public CommentEntriesAdapter(Context context, ArrayList<CommentEntries> comments) {
        super(context, R.layout.comment_item, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //1. Get the item
        //2. Create the xml template
        //3. Stick data into the XML template
        //4. Return the view

        // Get the data item for this position
        // Get the data item for this position
        CommentEntries comment = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment_item, parent, false);
        }
        // Lookup view for data population
        TextView tvComment = (TextView) convertView.findViewById(R.id.tvComment);
        TextView tvCommentUser = (TextView) convertView.findViewById(R.id.tvCommentUser);
        ImageView ivCommentUserProfilePic = (ImageView) convertView.findViewById(R.id.ivCommentUserProfilePic);

        Picasso.with(getContext()).load(comment.getCommentUserPicUrl()).into(ivCommentUserProfilePic);
        tvComment.setText(comment.getComment());
        tvCommentUser.setText(comment.getCommentUser());
        return convertView;
    }
}
