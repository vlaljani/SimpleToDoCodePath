package com.codepath.simpletodo;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;

/**
 * Created by vibhalaljani on 12/28/14.
 */
public class EditItemDialog extends DialogFragment implements View.OnClickListener {
    private EditText et_edit_item;
    private NumberPicker np_edit_item;
    private CheckBox cb_item_complete;
    private DatePicker dp_edit_item;

    public EditItemDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public void onClick(View v) {
        EditItemDialogListener listener = (EditItemDialogListener) getActivity();
        listener.onFinishEditDialog(et_edit_item.getText().toString(),
                                    np_edit_item.getValue(),
                                    cb_item_complete.isChecked(),
                                    new MyCustomDate(dp_edit_item.getDayOfMonth(),
                                                     dp_edit_item.getMonth(),
                                                     dp_edit_item.getYear()));
        dismiss();
    }

    public interface EditItemDialogListener {
        void onFinishEditDialog(String inputText, int priority, boolean complete,
                                MyCustomDate dueDate);
    }

    public static EditItemDialog newInstance(String currText,
                                             String title,
                                             int priority,
                                             int max_priority,
                                             int min_priority,
                                             int done,
                                             MyCustomDate dueDate) {
        EditItemDialog frag = new EditItemDialog();
        Bundle args = new Bundle();
        args.putString("currText", currText);
        args.putString("title", title);
        args.putInt("priority", priority);
        args.putInt("max_priority", max_priority);
        args.putInt("min_priority", min_priority);
        args.putInt("done", done);
        args.putInt("day", dueDate.getDayOfMonth());
        args.putInt("month", dueDate.getMonthOfYear());
        args.putInt("year", dueDate.getYear());
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_item, container);
        et_edit_item = (EditText) view.findViewById(R.id.et_edit_item);
        np_edit_item = (NumberPicker) view.findViewById(R.id.np_edit_item);
        cb_item_complete = (CheckBox) view.findViewById(R.id.cb_item_complete);
        dp_edit_item = (DatePicker) view.findViewById(R.id.dp_edit_item);
        dp_edit_item.setMinDate(System.currentTimeMillis() - 1000);

        Button btnSave = (Button) view.findViewById(R.id.btnSave);
        String currText = getArguments().getString("currText", "");
        String title = getArguments().getString("title", getString(R.string.Edit_Item_Label));
        int priority = getArguments().getInt("priority");
        int max_priority = getArguments().getInt("max_priority");
        int min_priority = getArguments().getInt("min_priority");
        int done = getArguments().getInt("done");
        int day = getArguments().getInt("day");
        int month = getArguments().getInt("month");
        int year = getArguments().getInt("year");
        getDialog().setTitle(title);

        et_edit_item.setText(currText);
        np_edit_item.setMaxValue(max_priority);
        np_edit_item.setMinValue(min_priority);
        np_edit_item.setValue(priority);
        dp_edit_item.updateDate(year, month, day);
        if (done == 1) {
            cb_item_complete.setChecked(true);
        }

        // Show soft keyboard automatically
        et_edit_item.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        //mEditText.setOnEditorActionListener(this);
        btnSave.setOnClickListener(this);

        return view;
    }

    // Fires whenever the textfield has an action performed
    // In this case, when the "Done" button is pressed
    // REQUIRES a 'soft keyboard' (virtual keyboard)
    /*public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            EditItemDialogListener listener = (EditItemDialogListener) getActivity();
            listener.onFinishEditDialog(et_edit_item.getText().toString());
            dismiss();
            return true;
        }
        return false;
    }*/
}
