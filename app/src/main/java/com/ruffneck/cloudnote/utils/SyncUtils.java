package com.ruffneck.cloudnote.utils;

import android.content.Context;
import android.widget.Toast;

import com.ruffneck.cloudnote.db.NoteBookDAO;
import com.ruffneck.cloudnote.db.NoteDAO;
import com.ruffneck.cloudnote.models.note.Note;
import com.ruffneck.cloudnote.models.note.NoteBook;
import com.ruffneck.cloudnote.service.SyncIntentService;

import java.util.List;

public class SyncUtils {

    public static void syncAllUnSyncNote(Context context) {

        List<NoteBook> notebookList = NoteBookDAO.getInstance(context).queryByUnsyncNote();
        SyncIntentService.syncNoteBook(context, notebookList);

        List<Note> noteList = NoteDAO.getInstance(context).queryByUnsyncNote();
        SyncIntentService.syncNote(context, noteList);


        Toast.makeText(context, "开始备份!", Toast.LENGTH_SHORT).show();
    }

    public static void syncNote(Context context, Note note) {
        SyncIntentService.syncNote(context, note);
    }
}
