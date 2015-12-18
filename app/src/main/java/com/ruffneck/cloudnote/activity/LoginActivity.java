package com.ruffneck.cloudnote.activity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.info.Constant;
import com.ruffneck.cloudnote.utils.PixelUtil;
import com.ruffneck.cloudnote.utils.SnackBarUtils;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements IUiListener {


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

    public static final int REQUEST_SIGNIN = 0x321;
    private Tencent mTencent;

    private SharedPreferences mPref;

    @OnClick(R.id.bt_signin)
    void signIn(View bt) {
        Intent intent = new Intent(this, SigninActivity.class);
        startActivityForResult(intent, REQUEST_SIGNIN);
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

        if (TextUtils.isEmpty(password) || password.length() < 5 || password.length() > 15) {
            SnackBarUtils.showSnackBar(viewShelter, "密码只能是6-16位字符", Snackbar.LENGTH_LONG, "好的");
            return;
        }

        AVUser.logInInBackground(userName, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    if (avUser != null) {
                        loginSucceed();
                    } else {
                        SnackBarUtils.showSnackBar(viewShelter, "登录失败", Snackbar.LENGTH_LONG, "好的");
                    }
                } else {
                    JSONObject jsonObject = JSON.parseObject(e.getMessage());
                    String error = jsonObject.getString("error");

                    SnackBarUtils.showSnackBar(viewShelter, error, Snackbar.LENGTH_LONG, "好的");
                }
            }
        });

    }

    protected void loginSucceed() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.bt_qq)
    void onQQLogin(View view) {
        mTencent = Tencent.createInstance(Constant.QQ_APPID, this);
//        mTencent.setOpenId("E0431F034A8EC56B40B5A7CCFAAE59BD");
//        mTencent.setAccessToken("A212D17AC9AB8A3CF2623274CE4C30C4", String.valueOf(System.currentTimeMillis() + Long.parseLong("7776000") * 1000));
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", this);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        mPref = getSharedPreferences("config", MODE_PRIVATE);


        AVUser currentUser = AVUser.getCurrentUser();

        if (currentUser != null) {
            loginSucceed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SIGNIN:
                    String username = data.getStringExtra("username");
                    tilUsername.getEditText().setText(username);
                    SnackBarUtils.showSnackBar(viewShelter, "注册成功", Snackbar.LENGTH_LONG, "好的");
                    break;
                case Constants.REQUEST_LOGIN:
                    Tencent.onActivityResultData(requestCode, resultCode, data, this);
                    break;
            }
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

    //Tencent callback interface.
    @Override
    public void onComplete(Object o) {
        //{"ret":0,
        // "openid":"E0431F034A8EC56B40B5A7CCFAAE59BD",
        // "access_token":"A212D17AC9AB8A3CF2623274CE4C30C4",
        // "pay_token":"EFEF9152698520D3460DBDE2DD9B5FE9",
        // "expires_in":7776000,
        // "pf":"desktop_m_qq-10000144-android-2002-",
        // "pfkey":"7c61e3db9b15a2ff40b653d90588f2b5",
        // "msg":"",
        // "login_cost":563,
        // "query_authority_cost":266,
        // "authority_cost":-221553967}

        JSONObject jsonObject = JSON.parseObject(o.toString());
        String openId = jsonObject.getString("openid");
        String access_token = jsonObject.getString("access_token");
        String expires_in = jsonObject.getString("expires_in");
        getQQUserInfo(openId, access_token, expires_in);
    }

    protected void getQQUserInfo(String openId, String access_token, String expires_in) {
        mTencent.setOpenId(openId);
        mTencent.setAccessToken(access_token, String.valueOf(System.currentTimeMillis() + Long.parseLong(expires_in) * 1000));
        UserInfo userInfo = new UserInfo(this, mTencent.getQQToken());
        userInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                //{"ret":0,
                // "msg":"",
                // "is_lost":0,
                // "nickname":"　　　Ruffneck",
                // "gender":"男",
                // "province":"广西",
                // "city":"钦州",
                // "figureurl":"http:\/\/qzapp.qlogo.cn\/qzapp\/1104952323\/E0431F034A8EC56B40B5A7CCFAAE59BD\/30",
                // "figureurl_1":"http:\/\/qzapp.qlogo.cn\/qzapp\/1104952323\/E0431F034A8EC56B40B5A7CCFAAE59BD\/50",
                // "figureurl_2":"http:\/\/qzapp.qlogo.cn\/qzapp\/1104952323\/E0431F034A8EC56B40B5A7CCFAAE59BD\/100",
                // "figureurl_qq_1":"http:\/\/q.qlogo.cn\/qqapp\/1104952323\/E0431F034A8EC56B40B5A7CCFAAE59BD\/40",
                // "figureurl_qq_2":"http:\/\/q.qlogo.cn\/qqapp\/1104952323\/E0431F034A8EC56B40B5A7CCFAAE59BD\/100",
                // "is_yellow_vip":"0","vip":"0","yellow_vip_level":"0","level":"0","is_yellow_year_vip":"0"}


                //FigureUrl 是用户的空间照片,figureUrl_qq是qq的照片.

                JSONObject jsonObject = JSON.parseObject(o.toString().replaceAll("\\\\", ""));
                String nickName = jsonObject.getString("nickname");
                String firgureUrl = jsonObject.getString("figureurl_qq_2");

                mPref.edit().putString("nickname", nickName).apply();
                mPref.edit().putString("figureUrl", firgureUrl).apply();

                System.out.println("nickName = " + nickName);
                System.out.println("firgureUrl = " + firgureUrl);

                SnackBarUtils.showSnackBar(viewShelter, "获取qq信息成功啦!", Snackbar.LENGTH_LONG, "好的");
            }

            @Override
            public void onError(UiError uiError) {
                SnackBarUtils.showSnackBar(viewShelter, "获取qq信息失败!", Snackbar.LENGTH_LONG, "好的");
            }

            @Override
            public void onCancel() {
                SnackBarUtils.showSnackBar(viewShelter, "取消获取qq信息!", Snackbar.LENGTH_LONG, "好的");
            }
        });
    }

    @Override
    public void onError(UiError uiError) {
        SnackBarUtils.showSnackBar(viewShelter, "获取qq信息失败!", Snackbar.LENGTH_LONG, "好的");
    }

    @Override
    public void onCancel() {
        SnackBarUtils.showSnackBar(viewShelter, "取消获取qq信息!", Snackbar.LENGTH_LONG, "好的");
    }
}
