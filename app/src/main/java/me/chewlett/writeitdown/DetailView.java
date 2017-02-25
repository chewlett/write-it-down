package me.chewlett.writeitdown;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DetailView extends AppCompatActivity {

    private long noteId;
    private TextView title;
    private TextView subject;
    private TextView body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        title = (TextView) findViewById(R.id.note_title);
        subject = (TextView) findViewById(R.id.note_subject);
        body = (TextView) findViewById(R.id.note_body);
        noteId = getIntent().getLongExtra("rowId", -1);

        FloatingActionButton delete = (FloatingActionButton) findViewById(R.id.delete_note_button);
        FloatingActionButton edit = (FloatingActionButton) findViewById(R.id.edit_note_button);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle("Delete note?");
                alert.setMessage("Are you sure that you want to delete this notes?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteNote();
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEdit();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor c = db.getNote(noteId);
        c.moveToFirst();
        if (c.getCount() > 0) {
            title.setText(c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_TITLE)));
            subject.setText(c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_SUBJECT)));
            body.setText(c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_BODY)));
        }
        db.close();
    }

    private void deleteNote() {
        DBAdapter db = new DBAdapter(this);
        db.open();
        db.deleteNote(noteId);
        db.close();
        Intent back = new Intent(this, MainActivity.class);
        back.putExtra("NoteDeleted", true);
        startActivity(back);
    }

    private void goToEdit() {
        Intent editAct = new Intent(this, EditActivity.class);
        editAct.putExtra("noteId", noteId);
        startActivity(editAct);
    }
}
