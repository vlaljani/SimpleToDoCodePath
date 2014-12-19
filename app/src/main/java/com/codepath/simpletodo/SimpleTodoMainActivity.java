package com.codepath.simpletodo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class SimpleTodoMainActivity extends ActionBarActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    String todoFileName = "todo.txt";
    private final int REQUEST_CODE = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_todo_main);

        // Create an ArrayList
        // Edit: No longer needed because we're handling it in readItems
        // items = new ArrayList<String>();

        // Get a handle to ListView
        lvItems = (ListView) findViewById(R.id.lvItems);

        readItems();
        // Create an ArrayAdapter
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);

        // Attach the adapter to ListView
        lvItems.setAdapter(itemsAdapter);

        // Invoke ListViewListener
        setupListViewListener();
    }

    /* Method for setting up the
       listener on the ListView
    */
    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(

                // Interface definition for a callback to be invoked
                // when an item in this list has been clicked and held

                // AdapterView is the parent for ListView and all other
                // views that are determined by an adapter
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick (AdapterView <?>adapter,
                                                    View item,
                                                    int pos,
                                                    long id) { //Callback method
                        // On an item long click

                        // remove item at that position
                        items.remove(pos);

                        // notify the adapter of data set changing so it can refresh itself
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true; //true implies that the callback consumed the long click
                    }
                });
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter,
                                               View item,
                                               int pos,
                                               long id) {
                        Intent i = new Intent(SimpleTodoMainActivity.this, EditItemActivity.class);
                        i.putExtra("itemPosition", pos);
                        i.putExtra("itemText", items.get(pos));
                        startActivityForResult(i, REQUEST_CODE);

                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String editedItemText = data.getExtras().getString("editedItemText");
            int pos = data.getExtras().getInt("itemPosition");
            // Toast the name to display temporarily on screen
            items.set(pos, editedItemText);
            itemsAdapter.notifyDataSetChanged();

            writeItems();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_simple_todo_main, menu);
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

    /* This method will be called when the 'Add Item'
       button is clicked.
     */
    public void onAddItem(View view) {
        // Get a handle to the EditText instance
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);

        // Get the text for the new item to add
        String newItemToAdd = etNewItem.getText().toString().trim();

        if (! newItemToAdd.isEmpty()) {
            // Add item to list
            items.add(newItemToAdd);
        }

        // Clear the EditText
        etNewItem.setText("");

        writeItems();
    }

    private void readItems() {

        File filesDir = getFilesDir(); //Absolute path for files created using openFileOutput
        File todoFile = new File(filesDir, todoFileName);
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, todoFileName);
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
