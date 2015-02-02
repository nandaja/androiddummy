package com.yahoo.gridimagesearchapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.yahoo.gridimagesearchapp.R;
import com.yahoo.gridimagesearchapp.models.SettingsData;

import java.util.ArrayList;
import java.util.Arrays;


public class SettingsActivity extends ActionBarActivity {

    Spinner imgSize;
    Spinner color;
    Spinner type;

    EditText site;

    SettingsData settings;
    private ArrayList<String> sizes;
    private ArrayList<String> colors;
    private ArrayList<String> types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sizes = new ArrayList(Arrays.asList((String[]) getResources().getStringArray(R.array.image_size_array)));
        colors = new ArrayList(Arrays.asList((String[]) getResources().getStringArray(R.array.image_color_array)));
        types = new ArrayList(Arrays.asList((String[]) getResources().getStringArray(R.array.image_type_array)));

        imgSize = (Spinner) findViewById(R.id.spImageSize);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sizes);
        imgSize.setAdapter(adapter);

        color = (Spinner) findViewById(R.id.spColor);
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colors);
        color.setAdapter(colorAdapter);

        type = (Spinner) findViewById(R.id.spTypes);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        type.setAdapter(typeAdapter);

        site = (EditText)findViewById(R.id.etSite);

        settings=(SettingsData) getIntent().getSerializableExtra("data");

        if(settings!=null) {
            imgSize.setSelection(sizes.indexOf(settings.getSize()));

            color.setSelection(colors.indexOf(settings.getColor()));

            type.setSelection(types.indexOf(settings.getType()));

            site.setText(settings.getSite());
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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

        Intent intent = new Intent();

        intent.putExtra("data", settings);
        setResult(RESULT_OK, intent);
        this.finish();

    }
}
