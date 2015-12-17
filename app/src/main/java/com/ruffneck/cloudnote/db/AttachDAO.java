package com.ruffneck.cloudnote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.ruffneck.cloudnote.models.note.attach.Attach;
import com.ruffneck.cloudnote.models.note.attach.AttachFactory;

import java.util.ArrayList;
import java.util.List;

public class AttachDAO {
    // Database fields
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public static AttachDAO attachDAO;
    private Context mContext;

    public synchronized static AttachDAO getInstance(Context context) {
        if (attachDAO == null)
            attachDAO = new AttachDAO(context);

        return attachDAO;
    }

    private AttachDAO(Context context) {
        mContext = context.getApplicationContext();
        dbHelper = new DBHelper(mContext);
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

    public void delete(Attach attach) {

        open();

        database.delete(DBConstants.Attach.TABLE_NAME, DBConstants.Attach.COLUMN_ID + "=?",
                new String[]{attach.getId() + ""});

        close();
    }

    public int deleteByNoteId(long id) {

        open();

        int raw = database.delete(DBConstants.Attach.TABLE_NAME, DBConstants.Attach.COLUMN_NOTE + "=?",
                new String[]{id + ""});

        close();

        return raw;
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

    @Nullable
    public Attach queryFirstByNoteId(long id) {
        open();

        Cursor cursor = database.query(DBConstants.Attach.TABLE_NAME, null,
                DBConstants.Attach.COLUMN_NOTE + "=?",
                new String[]{id + ""}, null, null, null);

        if (cursor != null) {
            Attach attach;
            if (cursor.moveToNext()) {
                attach = AttachFactory.getInstance().create(cursor.getInt(cursor.getColumnIndex(DBConstants.Attach.COLUMN_TYPE_ID)));
                attach.setId(cursor.getLong(cursor.getColumnIndex(DBConstants.Attach.COLUMN_ID)));
                attach.setNoteId(cursor.getLong(cursor.getColumnIndex(DBConstants.Attach.COLUMN_NOTE)));
                attach.setLocalURL(cursor.getString(cursor.getColumnIndex(DBConstants.Attach.COLUMN_LOCAL_URL)));
                return attach;
            }

            cursor.close();
        }

        close();

        return null;
    }

    public int queryCountByNoteId(long id) {

        open();

        Cursor cursor = database.query(DBConstants.Attach.TABLE_NAME, new String[]{DBConstants.Attach.COLUMN_ID},
                DBConstants.Attach.COLUMN_NOTE + "=?",
                new String[]{id + ""}, null, null, null);

        if (cursor != null) {

            int count = cursor.getCount();
            cursor.close();
            return count;
        }

        close();

        return 0;
    }

}
