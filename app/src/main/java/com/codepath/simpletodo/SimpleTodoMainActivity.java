package com.codepath.simpletodo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.widget.NumberPicker;


import com.activeandroid.content.ContentProvider;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SimpleTodoMainActivity extends FragmentActivity
        implements EditItemDialog.EditItemDialogListener {

    private ArrayList<String> items;
    private Cursor todoCursor;
    private TodoCursorAdapter todoAdapter;
    private ListView lvItems;
    private NumberPicker nPNewItem;
    String todoFileName = "todo.txt";
    private final int REQUEST_CODE = 7;

    private long currEditItemId;
    private int currEditItemPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_todo_main);

        // Get a handle to ListView
        lvItems = (ListView) findViewById(R.id.lvItems);

        readItemsFromSQLLite();
        nPNewItem =
                (NumberPicker) findViewById(R.id.nPNewItem);
        nPNewItem.setMaxValue(items.size() + 1);
        nPNewItem.setMinValue(0);
        nPNewItem.setWrapSelectorWheel(true);

        // Create an ArrayAdapter
        // Edit: No longer needed because we're using a Custom Cursor Adapter
        //itemsAdapter = new ArrayAdapter<String>(this,
        //        android.R.layout.simple_list_item_1, items);

        // Create our CustomCursorAdapter
        todoCursor = Item.fetchResultCursor();
        todoAdapter = new TodoCursorAdapter(this, todoCursor);

        // Attach cursor adapter to the ListView
        lvItems.setAdapter(todoAdapter);

        // Attach the adapter to ListView
        // Edit: No longer needed because we've attached the CustomCursorAdapter to the ListView
        //lvItems.setAdapter(itemsAdapter);

        // Invoke ListViewListener
        setupListViewListener();

        // Loading data with content providers
        SimpleTodoMainActivity.this.getSupportLoaderManager().initLoader(0, null,
                                                                    new LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle cursor) {
                return new CursorLoader(SimpleTodoMainActivity.this,
                        ContentProvider.createUri(Item.class, null),
                        null, null, null, null
                );
            }

            @Override
            public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
                ((TodoCursorAdapter)lvItems.getAdapter()).swapCursor(cursor);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> arg0) {
                ((TodoCursorAdapter)lvItems.getAdapter()).swapCursor(null);
            }
        });
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
                        String currItem = items.get(pos);
                        items.remove(pos);
                        nPNewItem.setMaxValue(items.size() + 1);

                        // notify the adapter of data set changing so it can refresh itself
                        // Edit: Now notifying the todoAdapter instead
                        // itemsAdapter.notifyDataSetChanged();
                        new Delete().from(Item.class).where("Name = ?", currItem).execute();
                        todoAdapter.notifyDataSetChanged();

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
                        Item editItem = new Select().from(Item.class)
                                                    .where("name = ?", items.get(pos))
                                                    .executeSingle();

                        // Edit: No longer needed because we're using fragment dialog instead
                        /*Intent i = new Intent(SimpleTodoMainActivity.this, EditItemActivity.class);
                        i.putExtra("itemPosition", pos);
                        i.putExtra("itemText", items.get(pos));
                        i.putExtra("id", editItem.getId());
                        startActivityForResult(i, REQUEST_CODE);*/

                        currEditItemId = editItem.getId();
                        currEditItemPos = pos;
                        showEditDialog(items.get(pos),
                                       editItem.priority,
                                       nPNewItem.getMaxValue(),
                                       nPNewItem.getMinValue(),
                                       editItem.done,
                                       editItem.dueDate);

                    }
                }
        );
    }

    private void showEditDialog(String currItem, int priority, int max_priority,
                                int min_priority, int done, MyCustomDate dueDate) {
        FragmentManager fm = getSupportFragmentManager();
        EditItemDialog editItemDialog = EditItemDialog.newInstance(currItem,
                                        getString(R.string.Edit_Item_Label), priority,
                                        max_priority, min_priority, done, dueDate);
        editItemDialog.show(fm, "fragment_edit_item");
    }

    public void onFinishEditDialog(String inputText, int priority, boolean complete,
                                   MyCustomDate dueDate) {

        items.set(currEditItemPos, inputText);

        Item editItem = Item.load(Item.class, currEditItemId);
        editItem.name = inputText;
        editItem.priority = priority;
        if (complete) {
            editItem.done = 1;
        } else {
            editItem.done = 0;
        }
        editItem.dueDate = dueDate;
        editItem.save();
        todoAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String editedItemText = data.getExtras().getString("editedItemText");
            int pos = data.getExtras().getInt("itemPosition");

            items.set(pos, editedItemText);
            // Edit: Now using todoAdapter
            //itemsAdapter.notifyDataSetChanged();

            Item editItem = Item.load(Item.class, data.getExtras().getLong("id"));
            editItem.name = editedItemText;
            editItem.save();
            todoAdapter.notifyDataSetChanged();
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
        int newItemPriority = nPNewItem.getValue();

        if (! newItemToAdd.isEmpty() && !items.contains(newItemToAdd)) {
            // Add item to list
            items.add(newItemToAdd);
            saveItemToSQLLite(newItemToAdd, newItemPriority);
        }

        // Clear the EditText
        etNewItem.setText("");
        nPNewItem.setMaxValue(items.size() + 1);

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

    private void readItemsFromSQLLite() {
        List<Item> readItems = new Select()
                .from(Item.class)
                .execute();
        items = new ArrayList<String>();
        if (!readItems.isEmpty()) {
            for (int i = 0; i < readItems.size(); i++) {
                items.add(i, readItems.get(i).name);
            }
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

    private void saveItemToSQLLite(String itemName, int priority) {
        Item item = new Item(itemName, priority);
        item.save();
        todoAdapter.notifyDataSetChanged();
    }


}
