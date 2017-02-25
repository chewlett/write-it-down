package me.chewlett.writeitdown;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton plus = (FloatingActionButton) findViewById(R.id.add_button);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddNote.class));
            }
        });
    }

    public void onResume() {
        super.onResume();
        DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor c = db.getAllNotes();
        c.moveToFirst();
        if (c.getCount() <= 0) {
            db.insertNote("Seeded Note 1", "Testing the app", "This is the body of seeded note 1");
            db.insertNote("Seeded Note 2", "Testing the app", "This is the body of seeded note 2");
            db.insertNote("Seeded Note 3", "Testing the app", "This is the body of seeded note 3");
            c = db.getAllNotes();
            setListView(c);
        }
        else {
            setListView(c);
        }
        db.close();

        boolean added = getIntent().getBooleanExtra("NoteAdded", false);
        boolean deleted = getIntent().getBooleanExtra("NoteDeleted", false);
        boolean updated = getIntent().getBooleanExtra("NoteUpdated", false);
        if (added) {
            Toast.makeText(this, "Note Added!", Toast.LENGTH_LONG).show();
            getIntent().removeExtra("NoteAdded");
        }
        else if(deleted) {
            Toast.makeText(this, "Note Deleted!", Toast.LENGTH_LONG).show();
            getIntent().removeExtra("NoteDeleted");
        }
        else if(updated) {
            Toast.makeText(this, "Note Updated!", Toast.LENGTH_LONG).show();
            getIntent().removeExtra("NoteUpdated");
        }
    }

    private void setListView(Cursor c) {
        ListView listView = (ListView) findViewById(R.id.notes_list);
        NoteAdapter adapter = new NoteAdapter(this, c);
        listView.setAdapter(adapter);
    }
}

