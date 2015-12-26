package com.ruffneck.cloudnote.utils;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;

public class AUtils {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Animator toCenterReveal(View view) {
        int width = view.getWidth();
        int height = view.getHeight();
        return ViewAnimationUtils.createCircularReveal(view
                , width / 2
                , height / 2
                , 0
                , Math.max(width, height));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Animator fromCenterReveal(View view) {
        int width = view.getWidth();
        int height = view.getHeight();
        return ViewAnimationUtils.createCircularReveal(view
                , width / 2
                , height / 2
                , Math.max(width, height)
                , 0);
    }
}
