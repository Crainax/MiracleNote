package com.ruffneck.cloudnote.activity;

import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.models.note.Note;
import com.ruffneck.cloudnote.utils.FormatUtils;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AlarmActivity extends AppCompatActivity {

    @InjectView(R.id.tv_alarm)
    TextView tvAlarm;
    @InjectView(R.id.tv_alarm_title)
    TextView tvAlarmTitle;
    @InjectView(R.id.tv_alarm_content)
    TextView tvAlarmContent;

    private Note note;
    private Ringtone ringtone;
    private Vibrator vibrator;

    @OnClick(R.id.bt_confirm)
    void onComfirm(View view) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ButterKnife.inject(this);

        initNote();

        initAlarm();
    }

    private void initAlarm() {

        Uri uri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM);

        ringtone = RingtoneManager.getRingtone(this, uri);
        setLooping(ringtone);

        ringtone.play();

        //Vibrate
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        vibrator.vibrate(new long[]{1000, 1000}, 0);

    }


    private void setLooping(Ringtone ringtone) {

        Class<Ringtone> clazz = Ringtone.class;

        try {
            Field mLocalPlayer = clazz.getDeclaredField("mLocalPlayer");
            mLocalPlayer.setAccessible(true);
            MediaPlayer mediaPlayer = (MediaPlayer) mLocalPlayer.get(ringtone);
            mediaPlayer.setLooping(true);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initNote() {

        note = getIntent().getParcelableExtra("note");
//        System.out.println("note = " + note);
        tvAlarm.setText(FormatUtils.formatDate(note.getAlarm()));
        tvAlarmTitle.setText(note.getTitle());
        tvAlarmContent.setText(note.getContent());
    }

    @Override
    protected void onDestroy() {
        if (ringtone != null) {
            ringtone.stop();
        }

        if (vibrator != null)
            vibrator.cancel();
        super.onDestroy();
    }
}
