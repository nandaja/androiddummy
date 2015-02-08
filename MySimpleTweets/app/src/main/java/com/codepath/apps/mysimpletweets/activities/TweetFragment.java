package com.codepath.apps.mysimpletweets.activities;

/**
 * Created by nandaja on 2/7/15.
 */

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class TweetFragment extends DialogFragment {

    User user;
    private TwitterClient client;

    TweetCallBack callBack;
    Button tweetButton;
    EditText etTweet;
    TextView tvCharCount;

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            tvCharCount.setText(String.valueOf(140-s.length()));
        }
    };

    public TweetFragment() {
        // Empty constructor required for DialogFragment
    }

    public static TweetFragment newInstance(String title) {
        TweetFragment frag = new TweetFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        callBack = (TweetCallBack) a;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Holo_Dialog_NoActionBar);
        final View view = getActivity().getLayoutInflater().inflate(R.layout.activity_tweet_fragment, null);
//
//       final Drawable d = new ColorDrawable(Color.WHITE);
//       d.setAlpha(130);

      //  dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

       // dialog.getWindow().setBackgroundDrawable(d);
        dialog.getWindow().setContentView(view);
      //  getActivity().getActionBar().hide();

      //  dialog.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.custom_fragment_title);
        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.TOP;
        params.verticalWeight=5.0f;
        dialog.setCanceledOnTouchOutside(true);
        client= TwitterApplication.getRestClient();


        return dialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tweet_fragment, container);
        getDialog().setCanceledOnTouchOutside(true);

        TextView tvUser = (TextView) view.findViewById(R.id.tvUser);
        TextView tvScName = (TextView) view.findViewById(R.id.tvScName);
        tweetButton = (Button) view.findViewById(R.id.tvTweetButton);
        tweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTweet(v);
            }
        });
        tvCharCount = (TextView) view.findViewById(R.id.tvCharCount);
        etTweet = (EditText) view.findViewById(R.id.etTweet);
        etTweet.addTextChangedListener(mTextEditorWatcher);
        user = (User) this.getArguments().get("user");
        tvUser.setText(user.getName());
        tvScName.setText("@" + user.getScreenName());
        ImageView ivUserPic = (ImageView) view.findViewById(R.id.ivUserPic);
        ivUserPic.setImageResource(android.R.color.transparent);
        Picasso.with(getActivity()).load(user.getProfilePicURL()).resize(75,75).into(ivUserPic);
        return view;
    }

    public void onTweet(View view) {


        client.postTweet(etTweet.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] header, JSONObject response) {
                Tweet tweet = Tweet.buildTweet(response);
                callBack.onTweetSaved(tweet);
                dismiss();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }


}
