package com.ruffneck.cloudnote.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;

import com.ruffneck.cloudnote.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ColorPicker extends Dialog implements SeekBar.OnSeekBarChangeListener {

    public interface OnConfirmListener {
        void onConfirm(int color);
    }

    private OnConfirmListener onConfirmListener;
    //Default color is black.
    private int color;

    @InjectView(R.id.viewColor)
    View viewColor;
    @InjectView(R.id.red)
    SeekBar sbRed;
    @InjectView(R.id.green)
    SeekBar sbGreen;
    @InjectView(R.id.blue)
    SeekBar sbBlue;
    @InjectView(R.id.bt_confirm_color)
    Button btConfirmColor;
    @InjectView(R.id.bt_cancel_color)
    Button btCancelColor;

    @OnClick(R.id.bt_confirm_color)
    void onConfirm(View view) {
        dismiss();
        if (onConfirmListener != null)
            onConfirmListener.onConfirm(color);
    }

    @OnClick(R.id.bt_cancel_color)
    void onCancel(View view) {
        dismiss();
    }

    public ColorPicker(Context context, int initColor, OnConfirmListener onConfirmListener) {
        super(context);
        this.onConfirmListener = onConfirmListener;
        this.color = initColor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_picker);
        ButterKnife.inject(this);

        sbBlue.setOnSeekBarChangeListener(this);
        sbRed.setOnSeekBarChangeListener(this);
        sbGreen.setOnSeekBarChangeListener(this);

        int red = (color >>16 ) & 0xff;
        int green = (color >>8 ) & 0xff;
        int blue = color & 0xff;

        sbRed.setProgress(red);
        sbGreen.setProgress(green);
        sbBlue.setProgress(blue);
        btCancelColor.setTextColor(color);
        btConfirmColor.setTextColor(color);
        viewColor.setBackground(new ColorDrawable(color));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        color = Color.rgb(sbRed.getProgress(), sbGreen.getProgress(), sbBlue.getProgress());
        viewColor.setBackground(new ColorDrawable(color));
        btCancelColor.setTextColor(color);
        btConfirmColor.setTextColor(color);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


}
