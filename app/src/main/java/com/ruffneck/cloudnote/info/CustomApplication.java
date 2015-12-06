package com.ruffneck.cloudnote.info;

import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by 佛剑分说 on 2015/12/2.
 */
public class CustomApplication extends Application {

    public static CustomApplication mCustomApplication;

    public static CustomApplication getInstance() {
        return mCustomApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mCustomApplication = this;

        initImageLoader();
    }


    /**
     * Initialize the image loader's configuration.
     */
    private void initImageLoader() {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(720,1280)
                .diskCacheExtraOptions(720,1280,null)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache())
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(70 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();

        ImageLoader.getInstance().init(config);

    }

}
