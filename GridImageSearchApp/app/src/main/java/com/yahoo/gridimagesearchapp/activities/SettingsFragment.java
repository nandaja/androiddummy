package com.yahoo.gridimagesearchapp.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Button;
import android.view.View.OnClickListener;

import com.yahoo.gridimagesearchapp.R;
import com.yahoo.gridimagesearchapp.models.SettingsData;

import java.util.ArrayList;
import java.util.Arrays;


public class SettingsFragment extends DialogFragment {


    Spinner imgSize;
    Spinner color;
    Spinner type;

    EditText site;

    SettingsData settings;
    private ArrayList<String> sizes;
    private ArrayList<String> colors;
    private ArrayList<String> types;

    SettingsCallBack callBack;

    public SettingsFragment() {
        // Empty constructor required for DialogFragment
    }

    public static SettingsFragment newInstance(String title) {
        SettingsFragment frag = new SettingsFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Activity a){
        super.onAttach(a);
        callBack = (SettingsCallBack) a;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Holo_NoActionBar_TranslucentDecor);
        //),android.R.style.Theme_Translucent_NoTitleBar);
        final View view = getActivity().getLayoutInflater().inflate(R.layout.activity_settings, null);

        final Drawable d = new ColorDrawable(Color.BLACK);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        d.setAlpha(200);

        dialog.getWindow().setBackgroundDrawable(d);
        dialog.getWindow().setContentView(view);

        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;

        dialog.setCanceledOnTouchOutside(true);


        return dialog;
    }

    /*@Override public void onStart() {
        super.onStart();

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_settings, container);

        getDialog().setCanceledOnTouchOutside(true);

        sizes = new ArrayList(Arrays.asList((String[]) getResources().getStringArray(R.array.image_size_array)));
        colors = new ArrayList(Arrays.asList((String[]) getResources().getStringArray(R.array.image_color_array)));
        types = new ArrayList(Arrays.asList((String[]) getResources().getStringArray(R.array.image_type_array)));

        imgSize = (Spinner) view.findViewById(R.id.spImageSize);
        color = (Spinner) view.findViewById(R.id.spColor);
        type = (Spinner) view.findViewById(R.id.spTypes);
        site = (EditText)view.findViewById(R.id.etSite);

        Button button = (Button) view.findViewById(R.id.btSubmit);
        button.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onSave(v);
                dismiss();
            }
        });

        settings = (SettingsData) this.getArguments().get("data");

      /*  settings=(SettingsData) getIntent().getSerializableExtra("data");*/

        if(settings!=null) {
            imgSize.setSelection(sizes.indexOf(settings.getSize()));

            color.setSelection(colors.indexOf(settings.getColor()));

            type.setSelection(types.indexOf(settings.getType()));

            site.setText(settings.getSite());
        }

        return view;
    }

    public void onSave(View view){

        settings = new SettingsData();
        //Read all available settings and set in object

        String size = imgSize.getSelectedItem().toString();
        if(!size.equals("Select size"))
            settings.setSize(size);

        String c = color.getSelectedItem().toString();
        if(!color.equals("Select color"))
            settings.setColor(c);

        String t = type.getSelectedItem().toString();
        if(!t.equals("Select type"))
            settings.setType(t);

        String s = site.getText().toString();
        if(s!=null && !s.isEmpty())
            settings.setSite(s);

        callBack.onSettingsSaved(settings);

    }
}