package com.example.morana.zeekimages.download;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.util.concurrent.ExecutorService;

/**
 * Created by Morana on 9/16/2017.
 */
public class ImagesCache {

    public static final int KB = 1024;
    public static final int DISK_CACHE_SIZE = KB * KB * 10; // 10MB
    private static final String TAG = ImagesCache.class.getSimpleName();

    private LruCache<String, Bitmap> imagesMemoryCache;
    private DiskCache imagesDiskCache;
    private final Object memoryCacheLock = new Object();




    public ImagesCache(Context context, ExecutorService executor) {
        // Initialize memory cache
        final int maxAvailableMemoryForApp = (int) (Runtime.getRuntime().maxMemory() / KB);
        final int memoryCacheSize = maxAvailableMemoryForApp / 8;
        imagesMemoryCache = new LruCache<>(memoryCacheSize);

        //Initialize disk cache
        imagesDiskCache = new DiskCache(context, executor);
    }


    public void addBitmapToCache(String url, Bitmap bitmap) {
        Log.d(TAG, "addBitmapToCache");
        // Add to memory cache as before
        if (fetchImageFromMemoryCache(url) == null) {
            Log.d(TAG, "add to memory cache");
            addDownloadedImageToMemoryCache(url, bitmap);
        }
        Log.d(TAG, "add to disk cache");
        addBitmapToDiskCache(url, bitmap);
    }



    public Bitmap getBitmapFromCache(String url) {
        Bitmap bitmap = fetchImageFromMemoryCache(url);
        if (bitmap != null) {
            Log.d(TAG, "BAM! had it in memory cache!");
            return bitmap;
        } else {
            Log.d(TAG, "Url was not found in memory cache. Let's check the Disk Cache..");
            bitmap =  getBitmapFromDiskCache(url);
            if (bitmap == null) {
                Log.d(TAG, "url not found in disk cache");
            } else {
                Log.d(TAG, "BAM2! had it in disk cache");
            }
            return bitmap;
        }

    }



    // MEMORY CACHE OPERATIONS

    private Bitmap fetchImageFromMemoryCache(String urlString) {
        synchronized (memoryCacheLock) {
            return imagesMemoryCache.get(urlString);
        }
    }

    private void addDownloadedImageToMemoryCache(String url, Bitmap bitmap) {
        synchronized(memoryCacheLock) {
            imagesMemoryCache.put(url, bitmap);
        }
    }

    // DISK CACHE OPERATIONS

    private Bitmap getBitmapFromDiskCache(String url) {
        return imagesDiskCache.getBitmapFromDisk(url);
    }


    private void addBitmapToDiskCache(String url, Bitmap bitmap) {
        imagesDiskCache.saveBitmapToDiskCache(url, bitmap);
    }





}
