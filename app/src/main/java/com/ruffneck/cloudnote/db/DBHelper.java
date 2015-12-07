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
                DBConstants.Note.COLUMN_NOTEBOOK + " integer," +
                DBConstants.Note.COLUMN_DATE_CREATE + " long," +
                DBConstants.Note.COLUMN_DATE_MODIFY + " long)");

        db.execSQL("create table " +
                DBConstants.NoteBook.TABLE_NAME + "(" +
                DBConstants.NoteBook.COLUMN_ID + " integer primary key autoincrement," +
                DBConstants.NoteBook.COLUMN_NAME + " text," +
                DBConstants.NoteBook.COLUMN_DETAIL + " text," +
                DBConstants.NoteBook.COLUMN_COLOR + " text)");

        db.execSQL("create table " +
                DBConstants.Type.TABLE_NAME + "(" +
                DBConstants.Type.COLUMN_ID + " integer primary key autoincrement," +
                DBConstants.Type.COLUMN_TYPE + " text)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
