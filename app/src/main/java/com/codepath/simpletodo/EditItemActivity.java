package com.codepath.simpletodo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class EditItemActivity extends ActionBarActivity {

    private EditText etmlEditItem;
    private int itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etmlEditItem = (EditText) findViewById(R.id.etmlEditItem);

        String itemText = getIntent().getStringExtra("itemText");
        itemPosition = getIntent().getIntExtra("itemPosition", 0);

        // Using append instead of setText puts the cursor at the end of
        // the text and in focus
        etmlEditItem.append(itemText);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
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

    public void onSave(View view) {
        Intent data = new Intent();

        data.putExtra("editedItemText", etmlEditItem.getText().toString());
        data.putExtra("itemPosition", itemPosition);
        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        this.finish();
    }
}
