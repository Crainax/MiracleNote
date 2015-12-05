package com.ruffneck.cloudnote.models.note.attach;

import android.os.Parcel;

public class ImageAttach extends Attach {

    private String localURL;
    private String netURL;

    protected ImageAttach(Parcel in) {
        localURL = in.readString();
        netURL = in.readString();
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(localURL);
        dest.writeString(netURL);
    }
}
