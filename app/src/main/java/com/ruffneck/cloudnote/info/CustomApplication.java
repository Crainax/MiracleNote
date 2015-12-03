package com.ruffneck.cloudnote.info;

import android.app.Application;

/**
 * Created by 佛剑分说 on 2015/12/2.
 */
public class CustomApplication extends Application {

    public static CustomApplication mCustomApplication;

    public static CustomApplication getInstance(){
        return mCustomApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mCustomApplication = this;
    }

}
