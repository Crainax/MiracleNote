package com.ruffneck.cloudnote.models.note.attach;

import android.os.Parcel;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

@AVClassName("Attach")
public class Attach extends AVObject {


    public static final String KEY_TYPE = "type";
    public static final String KEY_NOTE_ID = "noteId";
    public static final String KEY_ID = "attachId";
    private static final String KEY_LOCAL_URL = "localUrl";
    private static final String KEY_HAS_SYNC = "hasSync";

    public Attach() {
    }


    public Attach(String localURL, int type, long noteId) {
        super(Attach.class.getSimpleName());
//        System.out.println(Attach.class.getName());
        setLocalURL(localURL);
        setType(type);
        setNoteId(noteId);
    }

    public long getNoteId() {
        return getLong(KEY_NOTE_ID);
    }

    public void setNoteId(long noteId) {
        put(KEY_NOTE_ID, noteId);
    }

    public long getId() {
        return getLong(KEY_ID);
    }

    public void setId(long id) {
        put(KEY_ID, id, false);
    }

    public int getType() {
        return getInt(KEY_TYPE);
    }

    public void setType(int type) {
        put(KEY_TYPE, type);
    }

    public void setLocalURL(String localURL) {
        put(KEY_LOCAL_URL, localURL);
    }

    public String getLocalURL() {
        return getString(KEY_LOCAL_URL);
    }

    public boolean isHasSync() {
        return getBoolean(KEY_HAS_SYNC);
    }

    public void setHasSync(boolean hasSync) {
        put(KEY_HAS_SYNC, hasSync, false);
    }

    public Attach(Parcel in) {
        super(in);
    }

    public static final Creator CREATOR = AVObjectCreator.instance;

}