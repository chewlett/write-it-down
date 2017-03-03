package me.chewlett.writeitdown;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.GregorianCalendar;
import android.util.Log;

import java.util.Date;


/**
 * Created by Curtis Hewlett on 2/24/2017.
 */

public class DBAdapter {
    //various constants to be used in creating and updating the database
    static final String KEY_ROWID = "_id";
    static final String KEY_DATE = "created_at";
    static final String KEY_TITLE = "title";
    static final String KEY_SUBJECT = "subject";
    static final String KEY_BODY = "body";
    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "NotesDB";
    private static final String DATABASE_TABLE = "notes";
    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_CREATE =
            "CREATE TABLE "  + DATABASE_TABLE + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_DATE + " INTEGER NOT NULL, " + KEY_TITLE + " TEXT NOT NULL, "
                     + KEY_SUBJECT + " TEXT, " + KEY_BODY + " TEXT);";

//    private Context context;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
//        this.context = ctx;
//        dbHelper = new DatabaseHelper(context);
        dbHelper = new DatabaseHelper(ctx);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        public DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        //onCreate implicitly used to create the database table "contacts"
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //onUpgrade called implicitly when the database "version" has changed
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    //---opens the database---
    //calls SQLiteOpenHelper.getWritableDatabase() when opening the DB.
    //If the DB does not yet exist it will be created here.
    public DBAdapter open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close()
    {
        dbHelper.close();
    }

    //---insert a contact into the database---
    //uses ContentValues class to store key/value pairs for field names and data
    //to be inserted into the DB table by SQLiteDatabase.insert()
    public long insertNote(String title, String subject, String body)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_DATE, new Date().getTime());
        initialValues.put(KEY_SUBJECT, subject);
        initialValues.put(KEY_BODY, body);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular contact---
    //removes from the DB the record specified using SQLiteDatabase.delete()
    public boolean deleteNote(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---retrieves all the contacts---
    //SQLiteDatabase.query builds a SELECT query and returns a "Cursor" over the result set
    public Cursor getAllNotes(String sorting)
    {
        String sortBy = KEY_DATE + " DESC";
        switch(sorting) {
            case "date_dsc": sortBy = KEY_DATE + " DESC";
                break;
            case "date_asc": sortBy = KEY_DATE + " ASC";
                break;
            case "title": sortBy = KEY_TITLE + " COLLATE NOCASE ASC";
                break;
        }
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_DATE, KEY_TITLE,
                KEY_SUBJECT, KEY_BODY}, null, null, null, null, sortBy);
    }

    //---retrieves a particular contact---
    public Cursor getNote(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_DATE,
                                KEY_TITLE, KEY_SUBJECT, KEY_BODY}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a contact---
    //Uses SQLiteDatabase.update() to change existing data by key/value pairs
    public boolean updateNote(long rowId, String title, String subject, String body)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_SUBJECT, subject);
        args.put(KEY_BODY, body);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
