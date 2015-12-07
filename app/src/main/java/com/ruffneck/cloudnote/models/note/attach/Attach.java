package com.ruffneck.cloudnote.models.note.attach;

public abstract class Attach{

    public String localURL;
    public String netURL;
    protected int type;
    private long id;
    private long noteId;

    public Attach(){

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
}