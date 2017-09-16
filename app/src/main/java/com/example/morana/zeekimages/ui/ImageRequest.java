package com.example.morana.zeekimages.ui;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.morana.zeekimages.download.ImageFileDownloader;

import java.lang.ref.WeakReference;

/**
 * Created by Morana on 9/16/2017.
 */
public class ImageRequest implements ImageFileDownloader.TransactionListener {

    private String url;
    private WeakReference<ImageView> viewRef;

    public ImageRequest(String url, WeakReference<ImageView> viewRef) {
        this.url = url;
        this.viewRef = viewRef;
        viewRef.get().setTag(url);
    }

    public void fetch() {
        ImageFileDownloader.getInstance().getBitmapForUrl(url, new WeakReference<ImageFileDownloader.TransactionListener>(this));
    }


    @Override
    public void onBitmapDownloaded(String urlString, Bitmap bitmap) {
        if (viewRef!= null && viewRef.get()!=null && viewRef.get().getTag().equals(url)) {
            ImageView view = viewRef.get();
            view.setImageBitmap(bitmap);
        }
        DownloadOrganizer.remove(this);
    }

    @Override
    public void onError(String urlString) {
        DownloadOrganizer.remove(this);
    }
}
