package com.ruffneck.cloudnote.db;

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
