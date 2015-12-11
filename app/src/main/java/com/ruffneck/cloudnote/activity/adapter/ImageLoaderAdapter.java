package com.ruffneck.cloudnote.activity.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ruffneck.cloudnote.R;

public abstract class ImageLoaderAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected ImageLoader loader = ImageLoader.getInstance();
    protected DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.pic_loading)
            .showImageOnFail(R.drawable.pic_loading)
            .showImageOnFail(R.drawable.pic_loading)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();


}
