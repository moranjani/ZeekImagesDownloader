package com.example.morana.zeekimages;

import android.app.Application;

import com.example.morana.zeekimages.download.ImageFileDownloader;

/**
 * Created by Morana on 9/16/2017.
 */
public class ZeekImagesApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ImageFileDownloader.getInstance().init(this);
    }
}
