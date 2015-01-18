package com.yahoo.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/**
 * Edit Activity - used initially to model the edit functionality in To Do App
 */
public class EditActivity extends Activity {

    int position;
    String itemBeingEdited;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        EditText editText = (EditText) findViewById(R.id.editTextForItem);
        editText.requestFocus();
         itemBeingEdited = getIntent().getStringExtra("itemText");
         position = getIntent().getIntExtra("itemPosition", -1);
        editText.setText(itemBeingEdited);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
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

    public void onApplyEdit(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.editTextForItem);
        String editedItem = etNewItem.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("editedItemText", editedItem);
        intent.putExtra("position", position);
        setResult(100,intent);
        finish();
    }

}
