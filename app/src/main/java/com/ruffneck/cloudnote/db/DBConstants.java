package com.ruffneck.cloudnote.db;

import android.graphics.Color;

public class DBConstants  {

    /**
     * Notebook table column.
     */
    public class NoteBook{
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_COLOR = "color";
        public static final String COLUMN_DETAIL = "detail";
        public static final String TABLE_NAME = "notebook";
        public static final int ID_DEFAULT_NOTEBOOK = 1;
        public static final int ID_RECYCLE_BIN = 2;
        public static final String DEFAULT_NOTEBOOK_NAME = "默认笔记本";
        public static final String RECYCLE_BIN_NAME = "回收站";
        public static final String DEFAULT_NOTEBOOK_DETAIL = "存放未归类笔记的默认笔记本";
        public static final long DEFAULT_NOTEBOOK_COLOR = Color.BLUE;
        public static final String RECYCLE_BIN_DETAIL = "存放已经回收的笔记";
        public static final long RECYCLE_BIN_COLOR = Color.GREEN;
    }

    /**
     * Note table column.
     */
    public class Note{
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_DATE_ALARM = "date_alarm";
        public static final String COLUMN_DATE_MODIFY = "date_modify";
        public static final String COLUMN_DATE_CREATE = "date_create";
        public static final String COLUMN_NOTEBOOK = "notebook_id";
        public static final String TABLE_NAME = "note";
    }

    /**
     * Type table column.
     */
    public class Type{
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TYPE = "type";
        public static final int TYPE_IMAGE = 0x001;
        public static final String NAME_IMAGE = "image";
        public static final String TABLE_NAME = "type";
    }

    /**
     * Attach table column.
     */
    public class Attach{
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NOTE = "note";
        public static final String COLUMN_TYPE_ID = "type_id";
        public static final String COLUMN_LOCAL_URL = "local_url";
        public static final String TABLE_NAME = "attach";
    }

}
