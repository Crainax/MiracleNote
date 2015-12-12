package com.ruffneck.cloudnote.models.note.attach;

import com.ruffneck.cloudnote.db.DBConstants;

public class AttachFactory {

    private static AttachFactory attachFactory;

    private AttachFactory() {
    }

    public static synchronized AttachFactory getInstance() {
        if (attachFactory == null)
            attachFactory = new AttachFactory();

        return attachFactory;
    }

    public Attach create(int type) {
        if (type == DBConstants.Type.TYPE_IMAGE) {
            ImageAttach attach = new ImageAttach();
            attach.setType(type);
            return attach;
        }

        return null;
    }
}
