package com.codepath.simpletodo;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;



/**
 * Created by vibhalaljani on 12/25/14.
 */
@Table(name = "Items", id = BaseColumns._ID)
public class Item extends Model {

    @Column(name = "Name")
    public String name;

    @Column(name = "Priority")
    public int priority;

    @Column(name = "Done")
    public int done;

    @Column(name = "DueDate")
    public MyCustomDate dueDate;

    public Item(){
        super();
    }

    public Item(String name, int priority){
        super();
        this.name = name;
        this.priority = priority;
        this.done = 0;
        this.dueDate = new MyCustomDate();
    }

    public static Cursor fetchResultCursor() {
        String tableName = Cache.getTableInfo(Item.class).getTableName();
        // Query all items without any conditions
        String resultRecords = new Select(tableName + ".* ").
                from(Item.class).toSql();
        // Execute query on the underlying ActiveAndroid SQLite database
        Cursor resultCursor = Cache.openDatabase().rawQuery(resultRecords, null);
        return resultCursor;
    }

}
