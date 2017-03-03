package me.chewlett.writeitdown;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs;

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
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // get the default SharedPreferences object
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void onResume() {
        super.onResume();
        DBAdapter db = new DBAdapter(this);
        db.open();
        String sorting = prefs.getString("pref_sorting", "date_dsc");
        Cursor c = db.getAllNotes(sorting);
        c.moveToFirst();
        if (c.getCount() <= 0) {
            db.insertNote("Seeded Note 1", "Testing the app", "This is the body of seeded note 1");
            db.insertNote("Seeded Note 2", "Testing the app", "This is the body of seeded note 2");
            db.insertNote("Seeded Note 3", "Testing the app", "This is the body of seeded note 3");
            c = db.getAllNotes(sorting);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

