package com.ruffneck.cloudnote.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.ruffneck.cloudnote.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DoodleImageView extends ImageView implements View.OnTouchListener {

    private int width;
    private int height;
    private Context mContext;
    private Path mPath = new Path();
    private float mX;
    private float mY;
    private Paint mPaint;
    private Canvas mCanvas;

    private Bitmap bitmap;
    private int background = Color.WHITE;

    public DoodleImageView(Context context) {
        super(context);
        init(context);
    }


    public DoodleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DoodleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        setFocusable(true);
        setFocusableInTouchMode(true);
        setBackgroundColor(Color.WHITE);

        setOnTouchListener(this);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);

        mCanvas = new Canvas();

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        mCanvas.setBitmap(getBitmap());

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBitmap(bitmap);

        canvas.drawBitmap(bitmap, new Matrix(), null);

    }

    private void drawBitmap(Bitmap bitmap) {

        mCanvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(event);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(event);
                break;
            case MotionEvent.ACTION_UP:
                touchUp(event);
                break;
        }
        invalidate();
        return true;
    }

    private void touchStart(MotionEvent event) {

        mX = event.getX();
        mY = event.getY();

        mPath.moveTo(mX, mY);

    }

    private void touchMove(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (!(dx > 3 || dy > 3)) return;

        mPath.quadTo(mX, mY, (mX + x) / 2, (mY + y) / 2);

        mX = x;
        mY = y;
    }

    private void touchUp(MotionEvent event) {

        mPath = new Path();

    }

    public Bitmap getBitmap() {
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height + 200, Bitmap.Config.RGB_565);
            bitmap.eraseColor(background);
        }

        return bitmap;
    }

    public File save(String path,String fileName) throws IOException {

        FileUtils.mkRootDir(path);

        File file = new File(path+fileName);

        OutputStream os = new FileOutputStream(file);

        bitmap.compress(Bitmap.CompressFormat.PNG, 90, os);

        return file;
    }

    public void reset() {

        if (!bitmap.isRecycled())
            bitmap.recycle();

        invalidate();
    }

}
