package me.chewlett.writeitdown;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNote extends AppCompatActivity {

    private EditText title;
    private EditText subject;
    private EditText body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Button saveNote;

        title = (EditText) findViewById(R.id.title_edit);
        subject = (EditText) findViewById(R.id.subject_edit);
        body = (EditText) findViewById(R.id.body_edit);
        saveNote = (Button) findViewById(R.id.save_note);

        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });
    }

    private void addNote() {
        String title = this.title.getText().toString();
        String subject = this.subject.getText().toString();
        String body = this.body.getText().toString();

        if (!title.isEmpty()) {
            DBAdapter db = new DBAdapter(this);
            db.open();
            db.insertNote(title, subject, body);
            db.close();
            Intent back = new Intent(getApplicationContext(), MainActivity.class);
            back.putExtra("NoteAdded", true);
            startActivity(back);
        }
        else {
            Toast.makeText(this, "Need a title name!", Toast.LENGTH_LONG).show();
        }
    }
}
