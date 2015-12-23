package com.ruffneck.cloudnote.models.note.attach;

import com.avos.avoscloud.AVClassName;
import com.ruffneck.cloudnote.db.DBConstants;

@AVClassName("ImageAttach")
public class ImageAttach extends Attach {

    {
        setType(DBConstants.Type.TYPE_IMAGE);
    }

    public ImageAttach() {
    }


    public ImageAttach(String localURL, int type, long noteId) {
        super(localURL, type, noteId);
    }

    public static final Creator CREATOR = AVObjectCreator.instance;
}
