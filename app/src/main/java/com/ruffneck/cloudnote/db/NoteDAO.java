package com.ruffneck.cloudnote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ruffneck.cloudnote.models.note.Note;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        values.put(DBConstants.Note.COLUMN_TITLE, note.getTitle());
        values.put(DBConstants.Note.COLUMN_CONTENT, note.getContent());
        if (note.getAlarm() != null)
            values.put(DBConstants.Note.COLUMN_DATE_ALARM, note.getAlarm().getTime());
        if (note.getCreate() != null)
            values.put(DBConstants.Note.COLUMN_DATE_CREATE, note.getCreate().getTime());
        if (note.getModify() != null)
            values.put(DBConstants.Note.COLUMN_DATE_MODIFY, note.getModify().getTime());
        values.put(DBConstants.Note.COLUMN_NOTEBOOK, note.getNotebook());

        long id = database.insert(DBConstants.Note.TABLE_NAME, null, values);
        note.setId(id);
        close();

        return id;
    }

    public void update(Note note) {
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
        values.put(DBConstants.Note.COLUMN_NOTEBOOK, note.getNotebook());

        database.update(DBConstants.Note.TABLE_NAME, values, DBConstants.Note.COLUMN_ID + "=?", new String[]{note.getId() + ""});

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
     *
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

    public List<Note> queryByNoteBookId(long id) {

        open();

        List<Note> noteList = new ArrayList<>();
        Cursor cursor = database.query(DBConstants.Note.TABLE_NAME, null,
                DBConstants.Note.COLUMN_NOTEBOOK + "=?",
                new String[]{id + ""}, null, null, null);

        if (cursor != null) {
            Note note;
            while (cursor.moveToNext()) {
                note = new Note();
                note.setId(cursor.getLong(cursor.getColumnIndex(DBConstants.Note.COLUMN_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(DBConstants.Note.COLUMN_TITLE)));
                note.setContent(cursor.getString(cursor.getColumnIndex(DBConstants.Note.COLUMN_CONTENT)));
                note.setCreate(new Date(cursor.getLong(cursor.getColumnIndex(DBConstants.Note.COLUMN_DATE_CREATE))));
                note.setModify(new Date(cursor.getLong(cursor.getColumnIndex(DBConstants.Note.COLUMN_DATE_MODIFY))));
                note.setAlarm(new Date(cursor.getLong(cursor.getColumnIndex(DBConstants.Note.COLUMN_DATE_ALARM))));
                note.setNotebook(cursor.getInt(cursor.getColumnIndex(DBConstants.Note.COLUMN_NOTEBOOK)));
                noteList.add(note);
            }

            cursor.close();
        }

        close();

        return noteList;

    }
}
