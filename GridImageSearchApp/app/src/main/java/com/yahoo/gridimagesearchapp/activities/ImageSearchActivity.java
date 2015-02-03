package com.yahoo.gridimagesearchapp.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import android.view.Menu;
import android.view.*;
import android.view.View;
import android.view.MenuItem;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.support.v7.widget.SearchView;

import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.gridimagesearchapp.R;
import com.yahoo.gridimagesearchapp.adapters.ImageResultsAdapter;
import com.yahoo.gridimagesearchapp.models.ImageResult;
import com.yahoo.gridimagesearchapp.models.SettingsData;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;


public class ImageSearchActivity extends ActionBarActivity implements SettingsCallBack {

    private EditText etQuery;
    private StaggeredGridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter imageResultsAdapter;

    private static final int REQUEST_RESULT = 50;
    SettingsData settings;
    String query;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!isNetworkAvailable()){
            Toast.makeText(this, "Check internet connectivity", Toast.LENGTH_LONG).show();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        setUpViews();
        imageResults = new ArrayList<ImageResult>();
        imageResultsAdapter = new ImageResultsAdapter(this, imageResults);
        gvResults.setAdapter(imageResultsAdapter);

    }




    private void setUpViews() {
       // etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (StaggeredGridView) findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(ImageSearchActivity.this, ImageActivity.class);
                ImageResult image = imageResults.get(position);
                intent.putExtra("url", image.getFullURL());
                intent.putExtra("result", image);
                startActivity(intent);
            }
        });

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                // customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);

                System.out.println("NANDAJA :  offset is " + totalItemsCount + "page is " + page);
                if(totalItemsCount >= 64) {
                    return;
                }
                getImageData(totalItemsCount, false);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menu_image_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String q) {
                // perform query here
                query = q;
                getImageData(0, true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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


        if (id == R.id.miRequest) {
          /*  Intent settingIntent = new Intent(this, SettingsActivity.class);
            settingIntent.putExtra("data", settings);
            startActivityForResult(settingIntent,REQUEST_RESULT);*/
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            SettingsFragment settingsDialog = SettingsFragment.newInstance("Settings");
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", settings);
            settingsDialog.setArguments(bundle);
            settingsDialog.show(fm, "activity_settings");


        }

        return super.onOptionsItemSelected(item);

    }


    private void getImageData(int offset, final boolean clearPrevious) {
        AsyncHttpClient client = new AsyncHttpClient();
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?q=" + query + "&v=1.0&rsz=8";
        if(offset!=0)
            searchUrl = searchUrl + "&start=" + offset;
        if(settings!=null) {
            if (settings.getColor() != null) {
                searchUrl = searchUrl + "&imgcolor=" + settings.getColor();
            }
            if (settings.getSize() != null) {
                searchUrl = searchUrl + "&imgsz=" + settings.getSize();
            }
            if (settings.getType() != null) {
                searchUrl = searchUrl + "&imgtype=" + settings.getType();
            }
            if (settings.getSite() != null) {
                searchUrl = searchUrl + "&aa_sitesearch=" + settings.getSite();
            }
        }
        System.out.println("NANDAJA : Image search URL is : " + searchUrl);
        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONArray imageArray = response.getJSONObject("responseData").getJSONArray("results");
                    if(clearPrevious) {
                        imageResults.clear();//clear only for new search - do not clear while paginating
                        imageResultsAdapter.clear();
                    }

                    imageResults.addAll(ImageResult.fromJson(imageArray));
                    imageResultsAdapter.addAll(ImageResult.fromJson(imageArray));

                } catch (JSONException e) {

                }

            }

            @Override
            public void onFailure(final int statusCode, final Header[] headers, final String responseBody, final Throwable e){
                System.out.println("Command failed " + responseBody);
            }


        });
    }


    //Fires automatically when form data is posted
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Handle the form data
        if (requestCode == REQUEST_RESULT) {

            if (resultCode == RESULT_OK) {

                settings = (SettingsData) data.getSerializableExtra("data");
                getImageData(0, true);
            }

        }


    }

    public void onSettingsSaved(SettingsData s){

        settings = s;
        getImageData(0, true);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
