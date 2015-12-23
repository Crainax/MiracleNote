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
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.db.AttachDAO;
import com.ruffneck.cloudnote.db.DBConstants;
import com.ruffneck.cloudnote.db.NoteBookDAO;
import com.ruffneck.cloudnote.db.NoteDAO;
import com.ruffneck.cloudnote.models.note.Note;
import com.ruffneck.cloudnote.models.note.NoteBook;
import com.ruffneck.cloudnote.models.note.attach.Attach;
import com.ruffneck.cloudnote.models.note.attach.ImageAttach;
import com.ruffneck.cloudnote.utils.DateUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CloudIntentService extends IntentService {

    private static final String CLASS_NAME_FILE = "_File";

    private static final String ACTION_SYNC_NOTE = "com.ruffneck.cloudnote.service.action.SYNC_NOTE";
    private static final String ACTION_SYNC_NOTEBOOK = "com.ruffneck.cloudnote.service.action.ACTION_SYNC_NOTEBOOK";
    private static final String ACTION_SYNC_ATTACH = "com.ruffneck.cloudnote.service.action.ACTION_SYNC_ATTACH";
    private static final String ACTION_RESTORE_NOTE = "com.ruffneck.cloudnote.service.action.RESTORE_NOTE";
    private static final String ACTION_RESTORE_NOTEBOOK = "com.ruffneck.cloudnote.service.action.ACTION_RESTORE_NOTEBOOK";
    private static final String ACTION_RESTORE_ATTACH = "com.ruffneck.cloudnote.service.action.ACTION_RESTORE_ATTACH";
    private static final String ACTION_UPLOAD_ATTACH = "com.ruffneck.cloudnote.service.action.ACTION_UPLOAD_ATTACH";
    private static final String ACTION_DOWNLOAD_ATTACH = "com.ruffneck.cloudnote.service.action.ACTION_DOWNLOAD_ATTACH";

    private static final String EXTRA_NOTE = "com.ruffneck.cloudnote.service.extra.NOTE";
    private static final String EXTRA_NOTEBOOK = "com.ruffneck.cloudnote.service.extra.NOTEBOOK";
    private static final String EXTRA_ATTACH = "com.ruffneck.cloudnote.service.extra.ATTACH";

    public static final int NOTIFY_ID = 0x213;
    private static int taskCount = 0;
    private static int completeCount = 0;

    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNM;

    public CloudIntentService() {
        super("CloudIntentService");
    }

    public static void syncNote(Context context, List<Note> notes) {
        for (Note note : notes) {
            Intent intent = new Intent(context, CloudIntentService.class);
            intent.setAction(ACTION_SYNC_NOTE);
            intent.putExtra(EXTRA_NOTE, note);
            context.startService(intent);
        }
        taskCount += notes.size();
    }

    public static void syncNoteBook(Context context, List<NoteBook> noteBooks) {
        for (NoteBook noteBook : noteBooks) {
            Intent intent = new Intent(context, CloudIntentService.class);
            intent.setAction(ACTION_SYNC_NOTEBOOK);
            intent.putExtra(EXTRA_NOTEBOOK, noteBook);
            context.startService(intent);
        }
        taskCount += noteBooks.size();
    }

    public static void syncAttach(Context context, List<Attach> attaches) {
        for (Attach attach : attaches) {
            Intent intent = new Intent(context, CloudIntentService.class);
            intent.setAction(ACTION_SYNC_ATTACH);
            System.out.println("attach = " + attach);
            intent.putExtra(EXTRA_ATTACH, attach);
            context.startService(intent);
        }
        taskCount += attaches.size();
    }

    public static void restoreNote(Context context) {
        Intent intent = new Intent(context, CloudIntentService.class);
        intent.setAction(ACTION_RESTORE_NOTE);
        context.startService(intent);
        taskCount ++;
    }

    public static void restoreNoteBook(Context context) {
        Intent intent = new Intent(context, CloudIntentService.class);
        intent.setAction(ACTION_RESTORE_NOTEBOOK);
        context.startService(intent);
        taskCount ++;
    }

    public static void restoreAttach(Context context) {
        Intent intent = new Intent(context, CloudIntentService.class);
        intent.setAction(ACTION_RESTORE_ATTACH);
        context.startService(intent);
        taskCount ++;
    }

    public static void downloadAttach(Context context, Attach attach) {
        Intent intent = new Intent(context, CloudIntentService.class);
        intent.setAction(ACTION_DOWNLOAD_ATTACH);
        context.startService(intent);
        taskCount ++;
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
            } else if (ACTION_RESTORE_NOTE.equals(action)) {
                handleRestoreNote();
            } else if (ACTION_RESTORE_NOTEBOOK.equals(action)) {
                handleRestoreNoteBook();
            } else if (ACTION_RESTORE_ATTACH.equals(action)) {
                handleRestoreAttach();
            } else if (ACTION_DOWNLOAD_ATTACH.equals(action)) {
                final Attach param1 = intent.getParcelableExtra(EXTRA_ATTACH);
                handleDownloadAttach(param1);
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

        //judge whether the notebook is default notebook or not.
        if (noteBook.getId() == DBConstants.NoteBook.ID_DEFAULT_NOTEBOOK)
            avObject.put(DBConstants.NoteBook.MARK, DBConstants.NoteBook.ID_DEFAULT_NOTEBOOK);
        else if (noteBook.getId() == DBConstants.NoteBook.ID_RECYCLE_BIN)
            avObject.put(DBConstants.NoteBook.MARK, DBConstants.NoteBook.ID_RECYCLE_BIN);

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

        updateNotification("附件");

        try {
            attach.save();
        } catch (AVException e) {
            e.printStackTrace();
        }

        try {
            uploadAttach(attach);
        } catch (AVException | IOException e) {
            e.printStackTrace();
        }

        //Set the database that has sync completely.
        AttachDAO.getInstance(this).update(attach, true);
    }


    private void handleRestoreNote() {

        updateNotification("笔记:");

        AVQuery<AVObject> query = new AVQuery<>(DBConstants.Note.TABLE_NAME);
        try {
            List<AVObject> avObjectList = query.find();
            for (AVObject avObject : avObjectList) {
                Note note = new Note();
                note.setTitle(avObject.getString(DBConstants.Note.COLUMN_TITLE));
                note.setContent(avObject.getString(DBConstants.Note.COLUMN_CONTENT));
                note.setAlarm(avObject.getDate(DBConstants.Note.COLUMN_DATE_ALARM));
                note.setCreate(avObject.getDate(DBConstants.Note.COLUMN_DATE_CREATE));
                note.setModify(avObject.getDate(DBConstants.Note.COLUMN_DATE_MODIFY));
                note.setObjectId(avObject.getObjectId());
                note.setNotebook(avObject.getLong(DBConstants.Note.COLUMN_NOTEBOOK));
                note.setPreNotebook(avObject.getLong(DBConstants.Note.COLUMN_PRENOTEBOOK));
                NoteDAO.getInstance(this).restore(note);
            }
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    private void handleRestoreNoteBook() {

        updateNotification("笔记本:");
        AVQuery<AVObject> query = new AVQuery<>(DBConstants.NoteBook.TABLE_NAME);
        try {
            List<AVObject> avObjectList = query.find();
            for (AVObject avObject : avObjectList) {
                NoteBook notebook = new NoteBook();

                if (avObject.getInt(DBConstants.NoteBook.MARK) == DBConstants.NoteBook.ID_DEFAULT_NOTEBOOK)
                    notebook.setId(DBConstants.NoteBook.ID_DEFAULT_NOTEBOOK);
                else if (avObject.getInt(DBConstants.NoteBook.MARK) == DBConstants.NoteBook.ID_RECYCLE_BIN)
                    notebook.setId(DBConstants.NoteBook.ID_RECYCLE_BIN);

                notebook.setName(avObject.getString(DBConstants.NoteBook.COLUMN_NAME));
                notebook.setDetail(avObject.getString(DBConstants.NoteBook.DEFAULT_NOTEBOOK_DETAIL));
                notebook.setColor(avObject.getLong(DBConstants.NoteBook.COLUMN_COLOR));
                notebook.setObjectId(avObject.getObjectId());
                NoteBookDAO.getInstance(this).restore(notebook);
            }
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    private void handleRestoreAttach() {

        updateNotification("附件");

        AVQuery<ImageAttach> query = AVQuery.getQuery(ImageAttach.class);
        try {
            List<ImageAttach> attachList = query.find();
            System.out.println("attachList = " + attachList);
            for (Attach attach : attachList) {
                AttachDAO.getInstance(this).restore(attach);
                System.out.println("CloudIntentService.handleRestoreAttach");
                handleDownloadAttach(attach);
            }
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    public void uploadAttach(final Attach attach) throws AVException, IOException {

        AVQuery<AVObject> avQuery = new AVQuery<>(CLASS_NAME_FILE);
        avQuery.whereEqualTo("name", attach.getLocalURL());
        int count = avQuery.count();
        if (count != 0)
            return;

        updateNotification("上传图片中...." + attach.getLocalURL());

        AVFile avFile = AVFile.withAbsoluteLocalPath(attach.getLocalURL(), attach.getLocalURL());
        avFile.save();
    }

    private void handleDownloadAttach(final Attach attach) {
        File file = new File(attach.getLocalURL());
        if (file.exists())
            return;

        updateNotification("下载图片中...." + attach.getLocalURL());

        AVQuery<AVObject> avQuery = new AVQuery<>(CLASS_NAME_FILE);
        avQuery.whereEqualTo("name", attach.getLocalURL());
        List<AVObject> objectList;
        try {
            objectList = avQuery.find();
            for (AVObject object : objectList) {
                final String url = object.getString("url");
                final Thread thread = Thread.currentThread();
                HttpUtils httpUtils = new HttpUtils();
                httpUtils.download(url,
                        attach.getLocalURL()
                        , true, false,
                        new RequestCallBack<File>() {
                            @Override
                            public void onSuccess(ResponseInfo<File> responseInfo) {
                                thread.interrupt();
                                Toast.makeText(CloudIntentService.this, "附件下载成功!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                                Toast.makeText(CloudIntentService.this, "附件下载失败!", Toast.LENGTH_SHORT).show();
                                thread.interrupt();
                            }

                            @Override
                            public void onLoading(long total, long current, boolean isUploading) {
                                super.onLoading(total, current, isUploading);
                            }
                        });
                Thread.sleep(10000000);
            }
        } catch (AVException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        taskCount = 0;
        completeCount = 0;
        mNM.cancel(NOTIFY_ID);
        Toast.makeText(CloudIntentService.this, "完成备份!", Toast.LENGTH_SHORT).show();
    }


}
