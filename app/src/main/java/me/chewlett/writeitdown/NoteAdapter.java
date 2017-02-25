package me.chewlett.writeitdown;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Curtis Hewlett on 2/24/2017.
 */

public class NoteAdapter extends CursorAdapter {

    public NoteAdapter (Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.note_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        LinearLayout item = (LinearLayout) view.findViewById(R.id.list_item_container);
        item.setTag(cursor.getLong(cursor.getColumnIndexOrThrow(DBAdapter.KEY_ROWID)));
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long rowId = (long) v.getTag();
                Intent goToItem = new Intent(v.getContext(), DetailView.class);
                goToItem.putExtra("rowId", rowId);
                v.getContext().startActivity(goToItem);
            }
        });

        TextView titleView = (TextView) view.findViewById(R.id.note_title);
        TextView subjectView = (TextView) view.findViewById(R.id.note_subject);

        String title = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.KEY_TITLE));
        String subject = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.KEY_SUBJECT));

        titleView.setText(title);
        subjectView.setText(subject);
    }
}
