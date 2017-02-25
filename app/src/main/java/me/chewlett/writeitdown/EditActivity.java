package me.chewlett.writeitdown;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class EditActivity extends AppCompatActivity {

    private EditText title;
    private EditText subject;
    private EditText body;
    private long noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        title = (EditText) findViewById(R.id.title_edit);
        subject = (EditText) findViewById(R.id.subject_edit);
        body = (EditText) findViewById(R.id.body_edit);
        noteId = getIntent().getLongExtra("noteId", -1);

        Button save = (Button) findViewById(R.id.save_note);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
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

    private void saveNote() {
        String title = this.title.getText().toString();
        String subject = this.subject.getText().toString();
        String body = this.body.getText().toString();

        if (!title.isEmpty()) {
            DBAdapter db = new DBAdapter(this);
            db.open();
            db.updateNote(noteId, title, subject, body);
            db.close();
            Intent back = new Intent(getApplicationContext(), MainActivity.class);
            back.putExtra("NoteUpdated", true);
            startActivity(back);
        }
        else {
            Toast.makeText(this, "Need a title name!", Toast.LENGTH_LONG).show();
        }
    }

}
