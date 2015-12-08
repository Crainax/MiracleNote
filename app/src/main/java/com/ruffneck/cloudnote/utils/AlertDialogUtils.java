package com.ruffneck.cloudnote.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

public class AlertDialogUtils {

    public static AlertDialog show(Context context, String title, String message, String ok,
                            String cancel, final OkCallBack okCallBack, final CancelCallBack cancelCallBack) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title))
            builder.setTitle(title);
        if (!TextUtils.isEmpty(message))
            builder.setMessage(message);
        if (!TextUtils.isEmpty(ok))
            builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (okCallBack != null) {
                        okCallBack.onOkClick(dialog, which);
                    }
                    dialog.dismiss();
                }
            });
        if (!TextUtils.isEmpty(ok))
            builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (cancelCallBack != null) {
                        cancelCallBack.onCancelClick(dialog, which);
                    }
                }
            });
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }


    public interface OkCallBack {
        void onOkClick(DialogInterface dialog, int which);
    }


    public interface CancelCallBack {
        void onCancelClick(DialogInterface dialog, int which);
    }
}
