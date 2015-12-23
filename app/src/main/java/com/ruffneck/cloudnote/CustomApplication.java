package com.ruffneck.cloudnote;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ruffneck.cloudnote.activity.AlarmActivity;
import com.ruffneck.cloudnote.db.NoteDAO;
import com.ruffneck.cloudnote.info.Constant;
import com.ruffneck.cloudnote.models.note.Note;
import com.ruffneck.cloudnote.models.note.attach.Attach;
import com.ruffneck.cloudnote.models.note.attach.ImageAttach;
import com.ruffneck.cloudnote.utils.DateUtils;

import java.util.List;

public class CustomApplication extends Application {

    public static CustomApplication mCustomApplication;

    public static CustomApplication getInstance() {
        return mCustomApplication;
    }

    private List<PendingIntent> intentList;

    @Override
    public void onCreate() {
        super.onCreate();

        AVObject.registerSubclass(Attach.class);
        AVObject.registerSubclass(ImageAttach.class);
        AVOSCloud.initialize(this, Constant.LEANCLOUD_ID, Constant.LEANCLOUD_KEY);
        mCustomApplication = this;

        initImageLoader();

        initAlarm();
    }


    /**
     * Initialize the alarm in the background by the alarmManager.
     */
    private void initAlarm() {

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        NoteDAO noteDAO = NoteDAO.getInstance(this);
        List<Note> noteList = noteDAO.queryByAvailableAlarm(DateUtils.getCurrentDate().getTime());

        for (Note note : noteList) {
            Intent intent = new Intent(this, AlarmActivity.class);
            intent.putExtra("note", note);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) note.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            long time = note.getAlarm().getTime();
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
//            System.out.println("note = " + note.getTitle() + ":" + FormatUtils.formatDate(note.getAlarm()));
            note.setPendingIntent(pendingIntent);
//            System.out.println("pendingIntent = " + pendingIntent);
        }

    }

    public void updateAlarm(Note note) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (note.getAlarm().getTime() < DateUtils.getCurrentDate().getTime()) {
            if (note.getPendingIntent() != null) {
                alarmManager.cancel(note.getPendingIntent());
//                System.out.println("删除闹钟成功" + note.getTitle());
            }
            return;
        }

        Intent intent = new Intent(this, AlarmActivity.class);
        intent.putExtra("note", note);

        PendingIntent pendingIntent;
        if (note.getPendingIntent() == null)
            pendingIntent = PendingIntent.getActivity(this, (int) note.getId(), intent, 0);
        else
            pendingIntent = note.getPendingIntent();

        long time = note.getAlarm().getTime();
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
//        System.out.println("note = " + note.getTitle() + ":" + FormatUtils.formatDate(note.getAlarm()));
        note.setPendingIntent(pendingIntent);
//        System.out.println("pendingIntent = " + pendingIntent);

    }


    /**
     * Initialize the image loader's configuration.
     */
    private void initImageLoader() {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(720, 1280)
                .diskCacheExtraOptions(720, 1280, null)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache())
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(70 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();

        ImageLoader.getInstance().init(config);

    }

}
