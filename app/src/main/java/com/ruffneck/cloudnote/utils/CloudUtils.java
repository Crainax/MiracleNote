package com.ruffneck.cloudnote.utils;

import android.content.Context;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.SaveCallback;
import com.ruffneck.cloudnote.db.AttachDAO;
import com.ruffneck.cloudnote.db.NoteBookDAO;
import com.ruffneck.cloudnote.db.NoteDAO;
import com.ruffneck.cloudnote.models.note.Note;
import com.ruffneck.cloudnote.models.note.NoteBook;
import com.ruffneck.cloudnote.models.note.attach.Attach;
import com.ruffneck.cloudnote.service.CloudIntentService;

import java.io.IOException;
import java.util.List;

public class CloudUtils {

    public static void syncAllUnSyncData(Context context) {

        List<NoteBook> notebookList = NoteBookDAO.getInstance(context).queryByUnsyncNote();
        CloudIntentService.syncNoteBook(context, notebookList);

        List<Note> noteList = NoteDAO.getInstance(context).queryByUnsyncNote();
        CloudIntentService.syncNote(context, noteList);

        List<Attach> attachList = AttachDAO.getInstance(context).queryByUnsyncNote();
        CloudIntentService.syncAttach(context, attachList);

        Toast.makeText(context, "开始备份!", Toast.LENGTH_SHORT).show();
    }

    public static void UploadAttach(final Context context, final Attach attach) {
        AVQuery<AVObject> avQuery = new AVQuery<>("_File");
        avQuery.whereEqualTo("name", attach.getLocalURL());
        avQuery.countInBackground(new CountCallback() {
            @Override
            public void done(int i, AVException e) {
                if (i != 0) {
//                    Toast.makeText(context, "退出了", Toast.LENGTH_SHORT).show();
                    return;
                }

                AVFile avFile = null;
                try {
                    avFile = AVFile.withAbsoluteLocalPath(attach.getLocalURL(), "attach.getLocalURL()");
                    avFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
//                            System.out.println("done!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                            Toast.makeText(context, "gaga", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}
