package com.ruffneck.cloudnote.models.note.attach;

import android.os.Parcel;
import android.os.Parcelable;

import com.ruffneck.cloudnote.db.DBConstants;

public class ImageAttach extends Attach implements Parcelable{

    public ImageAttach() {
    }

    public ImageAttach(String localURL, int type, long noteId) {
        super(localURL, type, noteId);
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

    protected ImageAttach(Parcel in) {
        localURL = in.readString();
        netURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(localURL);
        dest.writeString(netURL);
        init();
    }

    @Override
    public String toString() {
        return "ImageAttach{" +
                "localURL='" + localURL + '\'' +
                ", netURL='" + netURL + '\'' +
                '}';
    }
}
