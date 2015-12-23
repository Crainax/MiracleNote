package com.ruffneck.cloudnote;

import android.test.AndroidTestCase;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.SaveCallback;

import java.io.IOException;

public class JavaTest extends AndroidTestCase {

    public void testCodeBlock() throws IOException {
//        AVFile avFile = AVFile.withAbsoluteLocalPath("haha.jpg", "/storage/emulated/0/Tencent/QQ_Images/1450367808616.jpeg");

        AVFile avFile = new AVFile("jujsadf","sd;lfjsadfdsfdksafd;sadf;lkdsfajsdaf".getBytes());
        avFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                System.out.println("done!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
        });
    }

}
