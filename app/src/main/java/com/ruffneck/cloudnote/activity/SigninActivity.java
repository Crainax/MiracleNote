package com.ruffneck.cloudnote.activity;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.ruffneck.cloudnote.R;

import butterknife.InjectView;
import butterknife.OnClick;


public class SigninActivity extends AppCompatActivity {


    @InjectView(R.id.til_username)
    TextInputLayout tilUsername;
    @InjectView(R.id.til_password)
    TextInputLayout tilPassword;
    @InjectView(R.id.til_confirm)
    TextInputLayout tilConfirm;
    @InjectView(R.id.til_email)
    TextInputLayout tilEmail;
    @InjectView(R.id.til_phone)
    TextInputLayout tilPhone;
    @InjectView(R.id.til_identify)
    TextInputLayout tilIdentify;
    @InjectView(R.id.bt_send_identify)
    Button btSendIdentify;
    TextInputLayout tilp;

    @OnClick(R.id.bt_send_identify)
    void sendIdentify(View view){

    }
    /**
     * Set the listener to the edittext
     */
    @SuppressWarnings("ConstantConditions")
    private void initListener() {

        /**
         * UserName's Listener
         */
        tilUsername.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    CheckUserName();
                }
            }
        });

        /**
         * Password's Listener.
         */
        tilPassword.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    CheckPassword();
                }
            }
        });

        /**
         * Password-Confirm's Listener.
         */
        tilConfirm.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    CheckPassword();
                }
            }
        });

        /**
         * Email's Listener.
         */
        tilEmail.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkEmail();
                }
            }
        });

        /**
         * Phone's Listener.
         */
        tilPhone.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkPhone();
                }
            }
        });

        /**
         * Identify's Listener.
         */
        tilIdentify.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkIdentify();
                }
            }
        });
    }

    private void checkIdentify() {

        String content = tilIdentify.getEditText().getText().toString();
        if (!TextUtils.isEmpty(content) && content.length() == 6) {
            tilIdentify.setErrorEnabled(false);
        } else {
            tilIdentify.setErrorEnabled(true);
            tilIdentify.setError("验证码是6位数字");
        }
    }

    private void checkPhone() {

        String content = tilPhone.getEditText().getText().toString();
        if (!TextUtils.isEmpty(content) && content.length() == 11) {
            tilPhone.setErrorEnabled(false);
        } else{
            tilPhone.setErrorEnabled(true);
            tilPhone.setError("手机只能是11位数字");
        }
    }

    private void checkEmail() {

        String content = tilEmail.getEditText().getText().toString();
        if (!TextUtils.isEmpty(content) && content.matches("\\w+@\\w+(\\.\\w{2,4})+")) {
            tilEmail.setErrorEnabled(false);
        } else {
            tilEmail.setErrorEnabled(true);
            tilEmail.setError("邮箱格式不正确");
        }
    }

    private void CheckPassword() {
        if (!isPasswordValid()) {
            tilPassword.setErrorEnabled(true);
            tilPassword.setError("密码必须6-16位");
        } else
            tilPassword.setErrorEnabled(false);

        if (!isTwicePasswordMatch()) {
            tilConfirm.setErrorEnabled(true);
            tilConfirm.setError("两次密码不一致");
        } else
            tilConfirm.setErrorEnabled(false);
    }

    private void CheckUserName() {
        if (TextUtils.isEmpty(tilUsername.getEditText().getText())) {
            tilUsername.setErrorEnabled(true);
            tilUsername.setError("用户名不能为空");
        } else
            tilUsername.setErrorEnabled(false);
    }

    /**
     * Set the Screen in the state that no title bar/
     */
    private void fullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    /**
     * @return if the Password EditText's Pattern valid.
     */
    private boolean isPasswordValid() {

        String content = tilPassword.getEditText().getText().toString();

        if (content == null)
            return false;

        return !(content.length() < 6 || content.length() > 15);

    }

    /**
     * @return if the Password Confirm EditText's Pattern valid.
     */
    private boolean isTwicePasswordMatch() {

        String password = tilPassword.getEditText().getText().toString();
        String confirm = tilConfirm.getEditText().getText().toString();

        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm))
            return false;

        return password.matches(confirm);
    }

    /**
     * @return if the phone EditText's Pattern valid.
     */
    private boolean isPhoneValid() {


        return true;
    }

    /**
     * @return if the identify EditText's Pattern valid.
     */
    private boolean isIdentifyValid() {

        if (TextUtils.isEmpty(tilUsername.getEditText().getText()))
            return false;

        return true;
    }
}
