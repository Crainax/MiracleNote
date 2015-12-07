package com.ruffneck.cloudnote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ruffneck.cloudnote.models.note.Note;

public class NoteDAO {

    // Database fields
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public static NoteDAO noteDAO;

    public synchronized static NoteDAO getInstance(Context context) {
        if (noteDAO == null)
            noteDAO = new NoteDAO(context);

        return noteDAO;
    }

    private NoteDAO(Context context) {
        dbHelper = new DBHelper(context.getApplicationContext());
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insert(Note note) {
        open();

        ContentValues values = new ContentValues();

        if (note.getId() != 0)
            values.put(DBConstants.Note.COLUMN_ID, note.getId());
        values.put(DBConstants.Note.COLUMN_TITLE, note.getTitle());
        values.put(DBConstants.Note.COLUMN_CONTENT, note.getContent());
        if (note.getAlarm() != null)
            values.put(DBConstants.Note.COLUMN_DATE_ALARM, note.getAlarm().getTime());
        if (note.getCreate() != null)
            values.put(DBConstants.Note.COLUMN_DATE_CREATE, note.getCreate().getTime());
        if (note.getModify() != null)
            values.put(DBConstants.Note.COLUMN_DATE_MODIFY, note.getModify().getTime());

        long id = database.insert(DBConstants.Note.TABLE_NAME, null, values);
        close();

        return id;
    }

    public void update(Note note){
        open();
        ContentValues values = new ContentValues();

        values.put(DBConstants.Note.COLUMN_TITLE, note.getTitle());
        values.put(DBConstants.Note.COLUMN_CONTENT, note.getContent());
        if (note.getAlarm() != null)
            values.put(DBConstants.Note.COLUMN_DATE_ALARM, note.getAlarm().getTime());
        if (note.getCreate() != null)
            values.put(DBConstants.Note.COLUMN_DATE_CREATE, note.getCreate().getTime());
        if (note.getModify() != null)
            values.put(DBConstants.Note.COLUMN_DATE_MODIFY, note.getModify().getTime());

        database.update(DBConstants.Note.TABLE_NAME, values,DBConstants.Note.COLUMN_ID + "=?", new String[]{note.getId() + ""});

        close();
    }

    public boolean exist(long id) {

        open();
        Cursor cursor = database.query(DBConstants.Note.TABLE_NAME, null, DBConstants.Note.COLUMN_ID + "=?", new String[]{id + ""}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToNext())
                return true;
            cursor.close();
        }

        close();
        System.out.println("NoteDAO.exist================false");
        return false;
    }

    /**
     * First judge that if database have the data , insert it if none ,else update.
     * @param note
     * @return
     */
    public long insertUpdate(Note note) {
        long id = -1;
        if (!exist(note.getId())) {
            id = insert(note);
        } else {
        }

        return id;
    }

}
