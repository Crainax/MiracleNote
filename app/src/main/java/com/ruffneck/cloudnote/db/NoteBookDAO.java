package com.ruffneck.cloudnote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.ruffneck.cloudnote.models.note.NoteBook;

import java.util.ArrayList;
import java.util.List;

public class NoteBookDAO {

    // Database fields
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public static NoteBookDAO noteBookDAO;
    private Context mContext;

    public synchronized static NoteBookDAO getInstance(Context context) {
        if (noteBookDAO == null)
            noteBookDAO = new NoteBookDAO(context);
        return noteBookDAO;
    }

    private NoteBookDAO(Context context) {
        mContext = context.getApplicationContext();
        dbHelper = new DBHelper(mContext);
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

        setValuesByNoteBook(noteBook, values);

        long id = database.insert(DBConstants.NoteBook.TABLE_NAME, null, values);
        noteBook.setId(id);


        close();

        return id;
    }

    protected void setValuesByNoteBook(NoteBook noteBook, ContentValues values) {
        values.put(DBConstants.NoteBook.COLUMN_NAME, noteBook.getName());
        values.put(DBConstants.NoteBook.COLUMN_COLOR, noteBook.getColor());
        values.put(DBConstants.NoteBook.COLUMN_DETAIL, noteBook.getDetail());
        values.put(DBConstants.NoteBook.COLUMN_SYNC, noteBook.isHasSync() ? 1 : 0);
        if (!TextUtils.isEmpty(noteBook.getObjectId()))
            values.put(DBConstants.NoteBook.COLUMN_OBJECTID, noteBook.getObjectId());
    }

    public void update(NoteBook noteBook) {

        update(noteBook, false);
    }

    public void update(NoteBook noteBook, boolean hasSync) {
        open();
        ContentValues values = new ContentValues();

        noteBook.setHasSync(hasSync);
        setValuesByNoteBook(noteBook, values);

        database.update(DBConstants.NoteBook.TABLE_NAME, values, DBConstants.NoteBook.COLUMN_ID + "=?", new String[]{noteBook.getId() + ""});

        close();
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

    public boolean exist(String objectId) {

        open();
        Cursor cursor = database.query(DBConstants.NoteBook.TABLE_NAME, null, DBConstants.NoteBook.COLUMN_OBJECTID + "=?", new String[]{objectId}, null, null, null);

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
     * @param notebook
     * @return
     */
    public long insertUpdate(NoteBook notebook) {
        long id = -1;
        if (!exist(notebook.getId())) {
            id = insert(notebook);
        } else {
            update(notebook);
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
                setNoteBookByCursor(cursor, noteBook);
                noteBookList.add(noteBook);
            }

            cursor.close();
        }

        close();

        return noteBookList;
    }

    protected void setNoteBookByCursor(Cursor cursor, NoteBook noteBook) {
        noteBook.setId(cursor.getLong(cursor.getColumnIndex(DBConstants.NoteBook.COLUMN_ID)));
        noteBook.setColor(cursor.getLong(cursor.getColumnIndex(DBConstants.NoteBook.COLUMN_COLOR)));
        noteBook.setDetail(cursor.getString(cursor.getColumnIndex(DBConstants.NoteBook.COLUMN_DETAIL)));
        noteBook.setName(cursor.getString(cursor.getColumnIndex(DBConstants.NoteBook.COLUMN_NAME)));
        noteBook.setHasSync(cursor.getInt(cursor.getColumnIndex(DBConstants.NoteBook.COLUMN_SYNC)) == 1);
        noteBook.setObjectId(cursor.getString(cursor.getColumnIndex(DBConstants.NoteBook.COLUMN_OBJECTID)));

    }

    public List<NoteBook> queryAllNoteBook() {
        open();

        List<NoteBook> noteBookList = new ArrayList<>();
        Cursor cursor = database.query(DBConstants.NoteBook.TABLE_NAME, null,
                DBConstants.NoteBook.COLUMN_ID + "<>?",
                new String[]{DBConstants.NoteBook.ID_RECYCLE_BIN + ""}, null, null, null);

        if (cursor != null) {
            NoteBook noteBook;
            while (cursor.moveToNext()) {
                noteBook = new NoteBook();
                setNoteBookByCursor(cursor, noteBook);
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
                setNoteBookByCursor(cursor, noteBook);
            }

            cursor.close();
        }


        close();

        return noteBook;
    }

    public NoteBook delete(NoteBook noteBook) {
        open();

        database.delete(DBConstants.NoteBook.TABLE_NAME, DBConstants.NoteBook.COLUMN_ID + "=?", new String[]{noteBook.getId() + ""});

        close();

        return noteBook;
    }

    public void createObjectId(NoteBook notebook, String objectId) {

        notebook.setObjectId(objectId);
        update(notebook);

    }

    public List<NoteBook> queryByUnsyncNote() {

        open();

        List<NoteBook> noteBookList = new ArrayList<>();
        Cursor cursor = database.query(DBConstants.NoteBook.TABLE_NAME, null,
                DBConstants.NoteBook.COLUMN_SYNC + "=?",
                new String[]{0 + ""}, null, null, null);

        if (cursor != null) {
            NoteBook noteBook;
            while (cursor.moveToNext()) {
                noteBook = new NoteBook();
                setNoteBookByCursor(cursor, noteBook);
                noteBookList.add(noteBook);
            }

            cursor.close();
        }

        close();

        return noteBookList;
    }

    public void restore(NoteBook notebook) {
        if (notebook.getId() == DBConstants.NoteBook.ID_DEFAULT_NOTEBOOK ||
                notebook.getId() == DBConstants.NoteBook.ID_RECYCLE_BIN ||
                exist(notebook.getObjectId()))
            update(notebook, true);
        else {
            insert(notebook);
        }
    }
}
