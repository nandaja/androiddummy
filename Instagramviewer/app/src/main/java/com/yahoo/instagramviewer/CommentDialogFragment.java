package com.yahoo.instagramviewer;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class CommentDialogFragment extends DialogFragment {

    private static ArrayList<CommentEntries> commentData;

    public CommentDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static CommentDialogFragment newInstance(String title, ArrayList<CommentEntries> comments) {
        CommentDialogFragment frag = new CommentDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        commentData = comments;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container);
        CommentEntriesAdapter adapter = new CommentEntriesAdapter(getActivity(), commentData);
        ListView lvComments = (ListView) view.findViewById(R.id.lvComments);
        lvComments.setAdapter(adapter);
        String title = getArguments().getString("title", "Comments");
        getDialog().setTitle(title);
        getDialog().setCanceledOnTouchOutside(true);
        return view;
    }
}