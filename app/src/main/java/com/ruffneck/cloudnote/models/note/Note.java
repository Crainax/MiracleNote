package com.ruffneck.cloudnote.models.note;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Note implements Parcelable {

    private String title;
    private String content;
    private long id;
    private Date modify;
    private Date create;
    private Date alarm = new Date(0);
    private long notebook = 1;
    private boolean hasSync = false;
    private long preNotebook = 1;

    public Note() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        return id == note.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    protected Note(Parcel in) {
        title = in.readString();
        content = in.readString();
        id = in.readLong();
        notebook = in.readLong();
        create = new Date(in.readLong());
        modify = new Date(in.readLong());
        alarm = new Date(in.readLong());
        hasSync = in.readInt() == 1;
        preNotebook = in.readLong();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getModify() {
        return modify;
    }

    public void setModify(Date modify) {
        this.modify = modify;
    }

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public Date getAlarm() {
        return alarm;
    }

    public void setAlarm(Date alarm) {
        this.alarm = alarm;
    }

    public long getNotebook() {
        return notebook;
    }

    public void setNotebook(long notebook) {
        this.notebook = notebook;
    }

    public boolean isHasSync() {
        return hasSync;
    }

    public void setHasSync(boolean hasSync) {
        this.hasSync = hasSync;
    }

    public long getPreNotebook() {
        return preNotebook;
    }

    public void setPreNotebook(long preNotebook) {
        this.preNotebook = preNotebook;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeLong(id);
        dest.writeLong(notebook);
        dest.writeLong(create.getTime());
        dest.writeLong(modify.getTime());
        dest.writeLong(alarm.getTime());
        dest.writeInt(hasSync ? 1 : 0);
        dest.writeLong(preNotebook);
    }
}
