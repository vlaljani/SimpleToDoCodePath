package com.codepath.simpletodo;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.CursorAdapter;

/**
 * Created by vibhalaljani on 12/27/14.
 */
public class TodoCursorAdapter extends CursorAdapter {
    public TodoCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, final Cursor cursor) {

        if( cursor.getPosition() % 2 == 1 ) {
            view.setBackgroundColor(context.getResources().getColor(R.color.background_odd));
        }
        else {
            view.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        }

        // Find fields to populate in inflated template
        TextView tvBody = (TextView) view.findViewById(R.id.tvBody);
        TextView tvBodyDueDate = (TextView) view.findViewById(R.id.tvBodyDueDate);
        // Extract properties from cursor
        String body = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
        int priority = cursor.getInt(cursor.getColumnIndexOrThrow("Priority"));
        int done = cursor.getInt(cursor.getColumnIndexOrThrow("Done"));
        String dueDate = cursor.getString(cursor.getColumnIndexOrThrow("DueDate"));

        // Populate fields with extracted properties

        Spannable priorityString;
        switch (priority) {
            case 0: priorityString = new SpannableString("");
                    break;
            case 1: priorityString = new SpannableString("!!!");
                    priorityString.setSpan(new ForegroundColorSpan(Color.RED),
                            0, priorityString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
            case 2: priorityString = new SpannableString("!!");
                    priorityString.setSpan(new ForegroundColorSpan(Color.RED),
                            0, priorityString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
            case 3: priorityString = new SpannableString("!");
                    priorityString.setSpan(new ForegroundColorSpan(Color.RED),
                        0, priorityString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
            default: priorityString = new SpannableString("(P" + priority + ")");
                     priorityString.setSpan(new ForegroundColorSpan(Color.YELLOW),
                        0, priorityString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        Spannable bodyString = new SpannableString(" " + body);
        bodyString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, bodyString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (done == 1) {
            priorityString.setSpan(new StrikethroughSpan(), 0, priorityString.length(), 0);
            bodyString.setSpan(new StrikethroughSpan(), 0, bodyString.length(), 0);
        }

        tvBody.setText(priorityString);
        tvBody.append(bodyString);
        tvBodyDueDate.setText("due by " + dueDate);


    }

}
