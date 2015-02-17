package com.codepath.apps.mysimpletweets.activities;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileActivity extends ActionBarActivity {

    TwitterClient client;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        client = TwitterApplication.getRestClient();
        String screenName = getIntent().getExtras().getString("screenName");

        client.getUser(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] header, JSONObject response) {

                user = new User();
                user = User.buildUser(response);

                populateUserInfo(user);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                System.out.println();
            }
        }, screenName);

        // Create user timeline fragment

        if(savedInstanceState ==null) {
            UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
            Bundle bundle = new Bundle();
            bundle.putString("screenName", screenName);
            userTimelineFragment.setArguments(bundle);

            //Display user fragment dynamically
            FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.flContainer, userTimelineFragment);
            ft.commit();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateUserInfo(User user){
        TextView tweets = (TextView) findViewById(R.id.tweets);
        TextView followers = (TextView) findViewById(R.id.followers);
        TextView following = (TextView) findViewById(R.id.following);

        TextView tvUser = (TextView) findViewById(R.id.tvUser);
        TextView tvScName = (TextView) findViewById(R.id.tvScName);

        tweets.setText(String.valueOf(user.getTweetsCount())+ " Tweets") ;
        followers.setText(String.valueOf(user.getFollowerCount()) + " Followers");
        following.setText(String.valueOf(user.getFollowingCount()) + " Following");
        tvUser.setText(user.getName());
        tvScName.setText(user.getScreenName());

        ImageView ivUserPic = (ImageView) findViewById(R.id.ivUserPic);
        ivUserPic.setImageResource(android.R.color.transparent);
        Picasso.with(this).load(user.getProfilePicURL()).resize(75, 75).into(ivUserPic);


        final View userHeader = findViewById(R.id.rlUserHeader);

        userHeader.setBackground(null);
        Picasso.with(this)
                .load(user.getProfile_background_image_url())
                .into(new Target() {
                    @Override
                    @TargetApi(16)
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        int sdk = android.os.Build.VERSION.SDK_INT;
                        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            userHeader.setBackgroundDrawable(new BitmapDrawable(bitmap));
                        } else {
                            userHeader.setBackground(new BitmapDrawable(getResources(), bitmap));
                        }
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        // use error drawable if desired
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        // use placeholder drawable if desired
                    }
                });



    }
}
