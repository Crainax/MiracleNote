package com.ruffneck.cloudnote.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.utils.SnackBarUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    private static final int REQUEST_PICK_RINGTONE = 0x806;

    @InjectView(R.id.tv_ringtone_content)
    TextView tvRingtoneContent;
    private SharedPreferences mPref;
    private String pickUri;

    @OnClick(R.id.rl_ringtone)
    void chooseRingtone(View view) {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        startActivityForResult(intent, REQUEST_PICK_RINGTONE, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        getWindow().setEnterTransition(new Slide());
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        mPref = getSharedPreferences("confg", MODE_PRIVATE);

        tvRingtoneContent.setText(mPref.getString("ringtone",
                RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE).toString()));
    }

    @Override
    protected int setContentResId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        getToolbar().setTitle("设置");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_RINGTONE:
                    pickUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI).toString();
                    mPref.edit().putString("ringtone", pickUri).apply();
                    tvRingtoneContent.setText(pickUri);
                    SnackBarUtils.showSnackBar(getToolbar(), "成功切换铃声!", 5000, "ok");
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishAfterTransition();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
