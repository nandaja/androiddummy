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
public class PhotoEntriesAdapter extends ArrayAdapter<PhotoEntries> {

    Context context;
    public PhotoEntriesAdapter(Context context, ArrayList<PhotoEntries> movies) {
        super(context, R.layout.photo_item, movies);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //1. Get the item
        //2. Create the xml template
        //3. Stick data into the XML template
        //4. Return the view
        // Get the data item for this position

        final PhotoEntries photo = getItem(position);
        ViewHolder holder;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.photo_item, parent, false);

        }
        holder=new ViewHolder();
        holder.setTvComments((TextView)convertView.findViewById(R.id.tvCommentCount));
        convertView.setTag(holder);

        //Bind a on-click listener to display the comments fragment
        ((ViewHolder)convertView.getTag()).getTvComments().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.support.v4.app.FragmentManager fm = ((PhotosActivity) getContext()).fetchFragmentManager();
                CommentDialogFragment commentDialog = CommentDialogFragment.newInstance("Comments", photo.getComments());
                commentDialog.show(fm, "fragment_comments");
            }
        });

        // Lookup view for data population
        ImageView ivGraphic = (ImageView) convertView.findViewById(R.id.ivGraphic);
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        TextView tvUploadTime = (TextView) convertView.findViewById(R.id.tvUploadTime);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        ImageView ivUserProfilePic = (ImageView) convertView.findViewById(R.id.ivUserProfilePic);
        TextView tvLikeCount = (TextView) convertView.findViewById(R.id.tvLikeCount);
        TextView tvCommentCount = (TextView) convertView.findViewById(R.id.tvCommentCount);

        //Populate data
        Picasso.with(getContext()).load(photo.getUrl()).into(ivGraphic);
        Picasso.with(getContext()).load(photo.getUserProfilePic()).into(ivUserProfilePic);

        tvCaption.setText(photo.getCaption());
        tvUploadTime.setText(photo.getDate());
        tvUserName.setText(photo.getUsername());
        tvLikeCount.setText(photo.getLikeCount() + " " + "likes");
        tvCommentCount.setText(photo.getCommentCount() + " comments");
        return convertView;
    }

    public static class ViewHolder{
        public TextView getTvComments() {
            return tvComments;
        }

        public void setTvComments(TextView tvComments) {
            this.tvComments = tvComments;
        }

        public TextView tvComments;

    }

}
