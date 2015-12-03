package com.ruffneck.cloudnote.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.utils.PixelUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {


    @InjectView(R.id.tvWelcome)
    TextView tvWelcome;
    @InjectView(R.id.viewDivider)
    View viewDivider;
    @InjectView(R.id.et_username)
    EditText etUsername;
    @InjectView(R.id.et_password)
    EditText etPassword;
    @InjectView(R.id.bt_signin)
    Button btSignin;
    @InjectView(R.id.bt_login)
    Button btLogin;
    @InjectView(R.id.bt_qq)
    Button btQq;
    @InjectView(R.id.ll_login)
    LinearLayout llLogin;
    @InjectView(R.id.view_shelter)
    View viewShelter;


    @OnClick(R.id.bt_signin)
    void signIn(View bt){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        showAnim();
    }


    /**
     * Set the Screen in the state that no title bar/
     */
    private void setFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    /**
     * Show the view's animation.
     */
    private void showAnim() {

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
