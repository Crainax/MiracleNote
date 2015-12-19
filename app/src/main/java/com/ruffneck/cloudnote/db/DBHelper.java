package com.ruffneck.cloudnote.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ruffneck.cloudnote.info.Constant;

public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context) {
        super(context, Constant.DATABASE_NAME, null, Constant.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " +
                DBConstants.Attach.TABLE_NAME + "(" +
                DBConstants.Attach.COLUMN_ID + " integer primary key autoincrement," +
                DBConstants.Attach.COLUMN_LOCAL_URL + " text," +
                DBConstants.Attach.COLUMN_NOTE + " integer," +
                DBConstants.Attach.COLUMN_TYPE_ID + " integer)");

        db.execSQL("create table " +
                DBConstants.Note.TABLE_NAME + "(" +
                DBConstants.Note.COLUMN_ID + " integer primary key autoincrement," +
                DBConstants.Note.COLUMN_TITLE + " text," +
                DBConstants.Note.COLUMN_CONTENT + " text," +
                DBConstants.Note.COLUMN_DATE_ALARM + " long," +
                DBConstants.Note.COLUMN_NOTEBOOK + " long," +
                DBConstants.Note.COLUMN_DATE_CREATE + " long," +
                DBConstants.Note.COLUMN_DATE_MODIFY + " long," +
                DBConstants.Note.COLUMN_PRENOTEBOOK + " long," +
                DBConstants.Note.COLUMN_SYNC + " int," +
                DBConstants.Note.COLUMN_OBJECTID + " text)");

        db.execSQL("create table " +
                DBConstants.NoteBook.TABLE_NAME + "(" +
                DBConstants.NoteBook.COLUMN_ID + " integer primary key autoincrement," +
                DBConstants.NoteBook.COLUMN_NAME + " text," +
                DBConstants.NoteBook.COLUMN_DETAIL + " text," +
                DBConstants.NoteBook.COLUMN_COLOR + " long," +
                DBConstants.NoteBook.COLUMN_SYNC + " int," +
                DBConstants.NoteBook.COLUMN_OBJECTID + " text)");

        db.execSQL("create table " +
                DBConstants.Type.TABLE_NAME + "(" +
                DBConstants.Type.COLUMN_ID + " integer primary key autoincrement," +
                DBConstants.Type.COLUMN_TYPE + " text)");


        //Insert the default data
        db.execSQL("insert into " + DBConstants.NoteBook.TABLE_NAME + " values(?,?,?,?,?,?)", new Object[]{
                DBConstants.NoteBook.ID_DEFAULT_NOTEBOOK,
                DBConstants.NoteBook.DEFAULT_NOTEBOOK_NAME,
                DBConstants.NoteBook.DEFAULT_NOTEBOOK_DETAIL,
                DBConstants.NoteBook.DEFAULT_NOTEBOOK_COLOR,
                0,
                ""
        });

        db.execSQL("insert into " + DBConstants.NoteBook.TABLE_NAME + " values(?,?,?,?,?,?)", new Object[]{
                DBConstants.NoteBook.ID_RECYCLE_BIN,
                DBConstants.NoteBook.RECYCLE_BIN_NAME,
                DBConstants.NoteBook.RECYCLE_BIN_DETAIL,
                DBConstants.NoteBook.RECYCLE_BIN_COLOR,
                0,
                ""
        });

        db.execSQL("insert into " + DBConstants.Type.TABLE_NAME + " values(?,?)", new Object[]{
                DBConstants.Type.TYPE_IMAGE,
                DBConstants.Type.NAME_IMAGE,
        });


//        db.execSQL("insert into " + DBConstants.NoteBook.TABLE_NAME + " values(" +
//                DBConstants.NoteBook.RECYCLE_BIN_NAME + "," +
//                DBConstants.NoteBook.RECYCLE_BIN_DETAIL + "," +
//                DBConstants.NoteBook.RECYCLE_BIN_COLOR + ")");
//
//        db.execSQL("insert into " + DBConstants.Type.TABLE_NAME + " values(" +
//                DBConstants.Type.TYPE_IMAGE + "," +
//                DBConstants.Type.NAME_IMAGE + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL("ALTER TABLE " + DBConstants.Note.TABLE_NAME + " ADD COLUMN " +
                        DBConstants.Note.COLUMN_PRENOTEBOOK + " long");
                db.execSQL("ALTER TABLE " + DBConstants.Note.TABLE_NAME + " ADD COLUMN " +
                        DBConstants.Note.COLUMN_SYNC + " integer");
                System.out.println("database version update from 1");
            case 2:
                db.execSQL("ALTER TABLE " + DBConstants.NoteBook.TABLE_NAME + " ADD COLUMN " +
                        DBConstants.NoteBook.COLUMN_SYNC + " integer");
                System.out.println("database version update from 2");
                break;
            case 3:
                db.execSQL("ALTER TABLE " + DBConstants.NoteBook.TABLE_NAME + " ADD COLUMN " +
                        DBConstants.NoteBook.COLUMN_OBJECTID + " text");
                db.execSQL("ALTER TABLE " + DBConstants.Note.TABLE_NAME + " ADD COLUMN " +
                        DBConstants.Note.COLUMN_OBJECTID + " text");
                System.out.println("database version update from 3");
        }
    }
}
