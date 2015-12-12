package com.ruffneck.cloudnote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ruffneck.cloudnote.models.note.attach.Attach;
import com.ruffneck.cloudnote.models.note.attach.AttachFactory;

import java.util.ArrayList;
import java.util.List;

public class AttachDAO {
    // Database fields
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public static AttachDAO attachDAO;

    public synchronized static AttachDAO getInstance(Context context) {
        if (attachDAO == null)
            attachDAO = new AttachDAO(context);

        return attachDAO;
    }

    private AttachDAO(Context context) {
        dbHelper = new DBHelper(context.getApplicationContext());
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insert(Attach attach) {
        open();

        ContentValues values = new ContentValues();

        values.put(DBConstants.Attach.COLUMN_TYPE_ID, attach.getType());
        values.put(DBConstants.Attach.COLUMN_LOCAL_URL, attach.localURL);
        values.put(DBConstants.Attach.COLUMN_NOTE, attach.getNoteId());

        long id = database.insert(DBConstants.Attach.TABLE_NAME, null, values);
        attach.setId(id);
        close();

        return id;
    }

    public boolean exist(long id) {

        open();
        Cursor cursor = database.query(DBConstants.Attach.TABLE_NAME, null, DBConstants.Attach.COLUMN_ID + "=?", new String[]{id + ""}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToNext())
                return true;
            cursor.close();
        }

        close();
//        System.out.println("AttachDAO.exist================false");
        return false;
    }


    /**
     * First judge that if database have the data , insert it if none ,else update.
     *
     * @param attach
     * @return
     */
    public long insertUpdate(Attach attach) {
        long id = -1;
        if (!exist(attach.getId())) {
            id = insert(attach);
        } else {
        }

        return id;
    }

    public List<Attach> queryByNoteId(long id) {

        open();

        List<Attach> attachList = new ArrayList<>();
        Cursor cursor = database.query(DBConstants.Attach.TABLE_NAME, null,
                DBConstants.Attach.COLUMN_NOTE + "=?",
                new String[]{id + ""}, null, null, null);

        if (cursor != null) {
            Attach attach;
            while (cursor.moveToNext()) {
                attach = AttachFactory.getInstance().create(cursor.getInt(cursor.getColumnIndex(DBConstants.Attach.COLUMN_TYPE_ID)));
                attach.setId(cursor.getLong(cursor.getColumnIndex(DBConstants.Attach.COLUMN_ID)));
                attach.setNoteId(cursor.getLong(cursor.getColumnIndex(DBConstants.Attach.COLUMN_NOTE)));
                attach.setLocalURL(cursor.getString(cursor.getColumnIndex(DBConstants.Attach.COLUMN_LOCAL_URL)));
                attachList.add(attach);
            }

            cursor.close();
        }

        close();

        return attachList;

    }
}
