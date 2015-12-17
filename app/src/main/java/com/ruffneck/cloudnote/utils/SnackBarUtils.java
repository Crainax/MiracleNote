package com.ruffneck.cloudnote.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackBarUtils {

    public static Snackbar showSnackBar(View view, String content, int Duration, String dismissString) {
        final Snackbar snackbar = Snackbar.make(view, content, Duration);
        snackbar.setAction(dismissString, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        }).show();

        return snackbar;
    }


}
