package com.ruffneck.cloudnote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.ruffneck.cloudnote.models.note.NoteBook;

import java.util.ArrayList;
import java.util.List;

public class NoteBookDAO {

    // Database fields
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public static NoteBookDAO noteBookDAO;

    public synchronized static NoteBookDAO getInstance(Context context) {
        if (noteBookDAO == null)
            noteBookDAO = new NoteBookDAO(context);
        return noteBookDAO;
    }

    private NoteBookDAO(Context context) {
        dbHelper = new DBHelper(context.getApplicationContext());
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insert(NoteBook noteBook) {
        open();

        ContentValues values = new ContentValues();

        values.put(DBConstants.NoteBook.COLUMN_NAME, noteBook.getName());
        values.put(DBConstants.NoteBook.COLUMN_COLOR, noteBook.getColor());
        values.put(DBConstants.NoteBook.COLUMN_DETAIL, noteBook.getDetail());

        long id = database.insert(DBConstants.NoteBook.TABLE_NAME, null, values);
        noteBook.setId(id);


        close();

        return id;
    }

    public boolean exist(long id) {

        open();
        Cursor cursor = database.query(DBConstants.NoteBook.TABLE_NAME, null, DBConstants.NoteBook.COLUMN_ID + "=?", new String[]{id + ""}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToNext())
                return true;
            cursor.close();
        }

        close();
        System.out.println("NoteBookDAO.exist================false");
        return false;
    }

    /**
     * First judge that if database have the data , insert it if none ,else update.
     *
     * @param note
     * @return
     */
    public long insertUpdate(NoteBook note) {
        long id = -1;
        if (!exist(note.getId())) {
            id = insert(note);
        } else {
        }

        return id;
    }

    public List<NoteBook> queryAll() {
        open();


        List<NoteBook> noteBookList = new ArrayList<>();
        Cursor cursor = database.query(DBConstants.NoteBook.TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null) {
            NoteBook noteBook;
            while (cursor.moveToNext()) {
                noteBook = new NoteBook();
                noteBook.setId(cursor.getLong(cursor.getColumnIndex(DBConstants.NoteBook.COLUMN_ID)));
                noteBook.setColor(cursor.getLong(cursor.getColumnIndex(DBConstants.NoteBook.COLUMN_COLOR)));
                noteBook.setDetail(cursor.getString(cursor.getColumnIndex(DBConstants.NoteBook.COLUMN_DETAIL)));
                noteBook.setName(cursor.getString(cursor.getColumnIndex(DBConstants.NoteBook.COLUMN_NAME)));
                noteBookList.add(noteBook);
            }

            cursor.close();
        }

        close();

        return noteBookList;
    }

    @Nullable
    public NoteBook queryById(long notebookId) {

        open();

        Cursor cursor = database.query(DBConstants.NoteBook.TABLE_NAME, null,
                DBConstants.NoteBook.COLUMN_ID + "=?", new String[]{notebookId + ""}, null, null, null);

        NoteBook noteBook = null;
        if (cursor != null) {
            if (cursor.moveToNext()) {
                noteBook = new NoteBook();
                noteBook.setId(cursor.getLong(cursor.getColumnIndex(DBConstants.NoteBook.COLUMN_ID)));
                noteBook.setColor(cursor.getLong(cursor.getColumnIndex(DBConstants.NoteBook.COLUMN_COLOR)));
                noteBook.setDetail(cursor.getString(cursor.getColumnIndex(DBConstants.NoteBook.COLUMN_DETAIL)));
                noteBook.setName(cursor.getString(cursor.getColumnIndex(DBConstants.NoteBook.COLUMN_NAME)));
            }

            cursor.close();
        }


        close();

        return noteBook;
    }

    public NoteBook delete(NoteBook noteBook){
        open();

        database.delete(DBConstants.NoteBook.TABLE_NAME, DBConstants.NoteBook.COLUMN_ID + "=?", new String[]{noteBook.getId() + ""});

        close();

        return noteBook;
    }

}
