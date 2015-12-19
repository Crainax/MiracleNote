package com.ruffneck.cloudnote.models.note.attach;

import android.os.Parcel;
import android.os.Parcelable;

import com.ruffneck.cloudnote.db.DBConstants;

public class ImageAttach extends Attach implements Parcelable {

    public ImageAttach() {
        init();
    }

    public ImageAttach(Parcel in) {
        super(in);
    }

    public ImageAttach(String localURL, int type, long noteId) {
        super(localURL, type, noteId);
        init();
    }

    private void init() {
        type = DBConstants.Type.TYPE_IMAGE;
    }

    public static final Creator<ImageAttach> CREATOR = new Creator<ImageAttach>() {
        @Override
        public ImageAttach createFromParcel(Parcel in) {
            return new ImageAttach(in);
        }

        @Override
        public ImageAttach[] newArray(int size) {
            return new ImageAttach[size];
        }

    };

    @Override
    public String toString() {
        return "ImageAttach{" +
                "localURL='" + localURL + '\'' +
                ", netURL='" + netURL + '\'' +
                '}';
    }
}
