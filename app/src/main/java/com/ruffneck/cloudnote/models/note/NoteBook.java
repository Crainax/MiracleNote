package com.ruffneck.cloudnote.models.note;

import android.os.Parcel;
import android.os.Parcelable;

public class NoteBook implements Parcelable {

    private long id = -1;
    private String name;
    private long color = 0x0000ff;
    private String detail;
    private boolean hasSync;
    private String objectId = "";


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteBook noteBook = (NoteBook) o;

        return id == noteBook.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getColor() {
        return color;
    }

    public void setColor(long color) {
        this.color = color;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean isHasSync() {
        return hasSync;
    }

    public void setHasSync(boolean hasSync) {
        this.hasSync = hasSync;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public NoteBook() {
    }

    public static final Parcelable.Creator<NoteBook> CREATOR
            = new Parcelable.Creator<NoteBook>() {
        public NoteBook createFromParcel(Parcel in) {
            return new NoteBook(in);
        }

        public NoteBook[] newArray(int size) {
            return new NoteBook[size];
        }
    };


    protected NoteBook(Parcel in) {
        id = in.readLong();
        name = in.readString();
        detail = in.readString();
        color = in.readLong();
        hasSync = in.readInt() == 1;
        objectId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(detail);
        dest.writeLong(color);
        dest.writeInt(hasSync ? 1 : 0);
        dest.writeString(objectId);
    }
}
