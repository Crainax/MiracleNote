package com.ruffneck.cloudnote.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.db.DBConstants;
import com.ruffneck.cloudnote.db.NoteBookDAO;
import com.ruffneck.cloudnote.db.NoteDAO;
import com.ruffneck.cloudnote.models.note.Note;
import com.ruffneck.cloudnote.models.note.NoteBook;
import com.ruffneck.cloudnote.models.note.attach.Attach;
import com.ruffneck.cloudnote.utils.DateUtils;

import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class SyncIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_SYNC_NOTE = "com.ruffneck.cloudnote.service.action.SYNC_NOTE";
    private static final String ACTION_SYNC_NOTEBOOK = "com.ruffneck.cloudnote.service.action.ACTION_SYNC_NOTEBOOK";
    private static final String ACTION_SYNC_ATTACH = "com.ruffneck.cloudnote.service.action.ACTION_SYNC_ATTACH";

    // TODO: Rename parameters
    private static final String EXTRA_NOTE = "com.ruffneck.cloudnote.service.extra.NOTE";
    private static final String EXTRA_NOTEBOOK = "com.ruffneck.cloudnote.service.extra.NOTEBOOK";
    private static final String EXTRA_ATTACH = "com.ruffneck.cloudnote.service.extra.ATTACH";
    public static final int NOTIFY_ID = 0x213;
    private static int taskCount = 0;
    private static int completeCount = 0;

    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNM;

    public SyncIntentService() {
        super("SyncIntentService");
    }

    public static void syncNote(Context context, List<Note> notes) {
        for (Note note : notes) {
            Intent intent = new Intent(context, SyncIntentService.class);
            intent.setAction(ACTION_SYNC_NOTE);
            intent.putExtra(EXTRA_NOTE, note);
            context.startService(intent);
        }
        taskCount += notes.size();
    }

    public static void syncNote(Context context, Note... notes) {
        for (Note note : notes) {
            Intent intent = new Intent(context, SyncIntentService.class);
            intent.setAction(ACTION_SYNC_NOTE);
            intent.putExtra(EXTRA_NOTE, note);
            context.startService(intent);
        }
        taskCount += notes.length;
    }

    public static void syncNoteBook(Context context, NoteBook... noteBooks) {
        for (NoteBook noteBook : noteBooks) {
            Intent intent = new Intent(context, SyncIntentService.class);
            intent.setAction(ACTION_SYNC_NOTEBOOK);
            intent.putExtra(EXTRA_NOTEBOOK, noteBook);
            context.startService(intent);
        }
        taskCount += noteBooks.length;
    }

    public static void syncNoteBook(Context context, List<NoteBook> noteBooks) {
        for (NoteBook noteBook : noteBooks) {
            Intent intent = new Intent(context, SyncIntentService.class);
            intent.setAction(ACTION_SYNC_NOTEBOOK);
            intent.putExtra(EXTRA_NOTEBOOK, noteBook);
            context.startService(intent);
        }
        taskCount += noteBooks.size();
    }

    /**
     * Starts this service to perform action sync note with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void syncAttach(Context context, Attach... attaches) {
        for (Attach attach : attaches) {
            Intent intent = new Intent(context, SyncIntentService.class);
            intent.setAction(ACTION_SYNC_ATTACH);
            intent.putExtra(EXTRA_ATTACH, attach);
            context.startService(intent);
        }
        taskCount += attaches.length;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        initNotification();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SYNC_NOTE.equals(action)) {
                final Note param1 = intent.getParcelableExtra(EXTRA_NOTE);
                handleSyncNote(param1);
            } else if (ACTION_SYNC_NOTEBOOK.equals(action)) {
                final NoteBook param1 = intent.getParcelableExtra(EXTRA_NOTEBOOK);
                handleSyncNoteBook(param1);
            } else if (ACTION_SYNC_ATTACH.equals(action)) {
                final Attach param1 = intent.getParcelableExtra(EXTRA_ATTACH);
                handleSyncAttach(param1);
            }
            completeCount++;
        }
    }

    private void initNotification() {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(), 0);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setWhen(DateUtils.getCurrentDate().getTime())
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .setContentTitle("备份中")
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_delete_black_24dp);
    }

    private void updateNotification(String string) {

        mBuilder.setContentText("进度:" + completeCount + "/" + taskCount + " - " + string)
                .setProgress(taskCount, completeCount, false);

        mNM.notify(NOTIFY_ID, mBuilder.build());
    }

    private void handleSyncNote(Note note) {

        if (TextUtils.isEmpty(note.getTitle()))
            updateNotification("笔记:" + note.getTitle());
        else
            updateNotification("笔记:");

        AVObject avObject;
        if (!TextUtils.isEmpty(note.getObjectId()))
            avObject = AVObject.createWithoutData(DBConstants.Note.TABLE_NAME, note.getObjectId());
        else
            avObject = new AVObject(DBConstants.Note.TABLE_NAME);

        avObject.put(DBConstants.Note.COLUMN_TITLE, note.getTitle());
        avObject.put(DBConstants.Note.COLUMN_CONTENT, note.getContent());
        avObject.put(DBConstants.Note.COLUMN_NOTEBOOK, note.getNotebook());
        avObject.put(DBConstants.Note.COLUMN_PRENOTEBOOK, note.getPreNotebook());
        avObject.put(DBConstants.Note.COLUMN_DATE_CREATE, note.getCreate());
        avObject.put(DBConstants.Note.COLUMN_DATE_MODIFY, note.getModify());
        avObject.put(DBConstants.Note.COLUMN_DATE_ALARM, note.getAlarm());
        try {
            avObject.save();
        } catch (AVException e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(note.getObjectId()))
            NoteDAO.getInstance(this).createObjectId(note, avObject.getObjectId());

        //Set the database that has sync completely.
        NoteDAO.getInstance(this).update(note, true);
    }

    private void handleSyncNoteBook(NoteBook noteBook) {
        if (TextUtils.isEmpty(noteBook.getName()))
            updateNotification("笔记本:" + noteBook.getName());
        else
            updateNotification("笔记本:");

        AVObject avObject;
        if (!TextUtils.isEmpty(noteBook.getObjectId()))
            avObject = AVObject.createWithoutData(DBConstants.NoteBook.TABLE_NAME, noteBook.getObjectId());
        else
            avObject = new AVObject(DBConstants.NoteBook.TABLE_NAME);

        avObject.put(DBConstants.NoteBook.COLUMN_NAME, noteBook.getName());
        avObject.put(DBConstants.NoteBook.COLUMN_DETAIL, noteBook.getDetail());
        avObject.put(DBConstants.NoteBook.COLUMN_COLOR, noteBook.getColor());
        try {
            avObject.save();
        } catch (AVException e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(noteBook.getObjectId()))
            NoteBookDAO.getInstance(this).createObjectId(noteBook, avObject.getObjectId());

        //Set the database that has sync completely.
        NoteBookDAO.getInstance(this).update(noteBook, true);

    }

    private void handleSyncAttach(Attach attach) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        taskCount = 0;
        completeCount = 0;
        mNM.cancel(NOTIFY_ID);
        Toast.makeText(SyncIntentService.this, "完成备份!", Toast.LENGTH_SHORT).show();
    }


}
