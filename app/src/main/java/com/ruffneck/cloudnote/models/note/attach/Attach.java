package com.ruffneck.cloudnote.models.note.attach;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class Attach implements Parcelable {

    public String localURL;
    public String netURL;
    protected int type;
    private long id;
    private long noteId;

    public Attach() {

    }

    public Attach(String localURL, int type, long noteId) {
        this.localURL = localURL;
        this.type = type;
        this.noteId = noteId;
    }

    public long getNoteId() {
        return noteId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setLocalURL(String localURL) {
        this.localURL = localURL;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(localURL);
        dest.writeString(netURL);
        dest.writeInt(type);
        dest.writeLong(id);
        dest.writeLong(noteId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected Attach(Parcel in) {
        localURL = in.readString();
        netURL = in.readString();
        type = in.readInt();
        id = in.readLong();
        noteId = in.readLong();
    }
}