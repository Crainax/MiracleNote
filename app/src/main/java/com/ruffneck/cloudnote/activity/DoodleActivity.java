package com.ruffneck.cloudnote.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.utils.AlertDialogUtils;
import com.ruffneck.cloudnote.utils.DateUtils;
import com.ruffneck.cloudnote.view.DoodleImageView;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DoodleActivity extends BaseActivity {

    @InjectView(R.id.iv_doodle)
    DoodleImageView ivDoodle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_doodle, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AlertDialogUtils.show(DoodleActivity.this, "确认", "确认离开吗?离开后涂鸦将不保存.", "确认", "取消", new AlertDialogUtils.OkCallBack() {
                    @Override
                    public void onOkClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }, null);
                break;
            case R.id.action_ok:
                URI uri = save();
                Intent intent = new Intent();
                intent.putExtra("uri", uri);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private URI save() {

        String filePath = "/sdcard/cloudnote/";
        String fileName = DateUtils.getCurrentDate().getTime() + ".png";

        File file = null;
        try {
            file = ivDoodle.save(filePath, fileName);
            Toast.makeText(DoodleActivity.this, file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            System.out.println("file = " + file.getAbsolutePath());
            return file.toURI();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(DoodleActivity.this, "保存失败!", Toast.LENGTH_SHORT).show();
        }


        return null;
    }

    @Override
    protected int setContentResId() {
        return R.layout.activity_doodle;
    }


}
