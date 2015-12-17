package com.ruffneck.cloudnote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.info.Constant;
import com.ruffneck.cloudnote.utils.PixelUtil;
import com.ruffneck.cloudnote.utils.SnackBarUtils;


import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {


    @InjectView(R.id.tvWelcome)
    TextView tvWelcome;
    @InjectView(R.id.viewDivider)
    View viewDivider;
    @InjectView(R.id.ll_login)
    LinearLayout llLogin;
    @InjectView(R.id.view_shelter)
    View viewShelter;
    @InjectView(R.id.til_username)
    TextInputLayout tilUsername;
    @InjectView(R.id.til_password)
    TextInputLayout tilPassword;


    @OnClick(R.id.bt_signin)
    void signIn(View bt) {
        Intent intent = new Intent(this, SigninActivity.class);
        startActivityForResult(intent, 0);
    }

    @OnClick(R.id.bt_login)
    void login(View view) {

        AVUser user = new AVUser();

        String userName = tilUsername.getEditText().getText().toString();
        String password = tilPassword.getEditText().getText().toString();


        if (TextUtils.isEmpty(userName)) {
            SnackBarUtils.showSnackBar(viewShelter, "用户名不能为空", Snackbar.LENGTH_LONG, "好的");
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 5 || password.length() >15) {
            SnackBarUtils.showSnackBar(viewShelter, "密码只能是6-16位字符", Snackbar.LENGTH_LONG, "好的");
            return;
        }

        AVUser.logInInBackground(userName, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if(e == null){
                    if(avUser != null){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        SnackBarUtils.showSnackBar(viewShelter,"登录失败", Snackbar.LENGTH_LONG,"好的");
                    }
                }else{
                    JSONObject jsonObject = JSON.parseObject(e.getMessage());
                    String error = jsonObject.getString("error");

                    SnackBarUtils.showSnackBar(viewShelter,error, Snackbar.LENGTH_LONG,"好的");
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        AVOSCloud.initialize(this, Constant.LEANCLOUD_ID, Constant.LEANCLOUD_KEY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == LoginActivity.RESULT_OK) {
            String username = data.getStringExtra("username");
            tilUsername.getEditText().setText(username);
            SnackBarUtils.showSnackBar(viewShelter, "注册成功", Snackbar.LENGTH_LONG, "好的");
        }
    }

    /**
     * Set the Screen in the state that no title bar/
     */
    private void setFullScreen() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }


    @Override
    protected void onResume() {
        super.onResume();
        showEnterAnim();
    }

    @Override
    protected void onPause() {
        super.onPause();
        showExitAnim();
    }

    /**
     * Show the view's animation when exit the animation.
     */
    private void showExitAnim() {

        final int DURATION = 400;

        llLogin.animate().alpha(0).setDuration(DURATION).setStartDelay(DURATION).start();

        tvWelcome.animate().alpha(0).setDuration(DURATION).setStartDelay(DURATION).start();

        viewDivider.animate().scaleX(0).setDuration(DURATION).setStartDelay(DURATION).start();

    }

    /**
     * Show the view's animation when enter the activity.
     */
    private void showEnterAnim() {

        final int INIT_DELAY = 400;
        final int DURATION = 400;

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        viewShelter.animate().translationY(-metrics.heightPixels + PixelUtil.dp2px(220)).setDuration(DURATION).setStartDelay(INIT_DELAY).start();

        llLogin.animate().alpha(1).setDuration(DURATION).setStartDelay(DURATION + INIT_DELAY).start();

        tvWelcome.animate().alpha(1).setDuration(DURATION).setStartDelay(DURATION + INIT_DELAY).start();

        viewDivider.animate().scaleX(1).setDuration(DURATION).setStartDelay(DURATION + INIT_DELAY).start();

    }
}
