package com.example.morana.zeekimages.download;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.io.File;

/**
 * Created by Morana on 9/16/2017.
 */
public class ImagesCache {

    private static final int KB = 1024;
    private static final String TAG = ImagesCache.class.getSimpleName();

    private Context context;
    private LruCache<String, Bitmap> imagesMemoryCache;
    private DiskCache diskCache;
    private final Object mDiskCacheLock = new Object();
    private final Object memoryCacheLock = new Object();
    private boolean mDiskCacheStarting = true;
    private static final int DISK_CACHE_SIZE = KB * KB * 10; // 10MB
    private static final String DISK_CACHE_SUBDIR = "thumbnails";



    public ImagesCache(Context context) {
        this.context = context;

        // Initialize memory cache
        final int maxAvailableMemoryForApp = (int) (Runtime.getRuntime().maxMemory() / KB);
        final int memoryCacheSize = maxAvailableMemoryForApp / 8;
        imagesMemoryCache = new LruCache<>(memoryCacheSize);

        // Initialize disk cache on background thread
//        File cacheDir = getDiskCacheDir(context, DISK_CACHE_SUBDIR);
       // new InitDiskCacheTask().execute(cacheDir);
    }





    class InitDiskCacheTask extends AsyncTask<File, Void, Void> {
        @Override
        protected Void doInBackground(File... params) {
            synchronized (mDiskCacheLock) {
                File cacheDir = params[0];
                //diskCache = DiskCache.open(cacheDir, DISK_CACHE_SIZE);
                mDiskCacheStarting = false; // Finished initialization
                mDiskCacheLock.notifyAll(); // Wake any waiting threads
            }
            return null;
        }
    }



    public void addBitmapToCache(String url, Bitmap bitmap) {
        // Add to memory cache as before
        if (fetchImageFromMemoryCache(url) == null) {
            addDownloadedImageToMemoryCache(url, bitmap);
        }
        addBitmapToDiskCache(url, bitmap);
    }



    public Bitmap getBitmapFromCache(String url) {
        Bitmap bitmap = fetchImageFromMemoryCache(url);
        if (bitmap != null) {
            Log.d(TAG, "BAM! had it in memory cache!");
            return bitmap;
        } else {
            Log.d(TAG, "Url was not found in memory cache. Let's check the Disk Cache..");
            return getBitmapFromDiskCache(url);
        }

    }





    /**
     * Creates a unique subdirectory of the designated app cache directory. Tries to use external
     but if not mounted, falls back on internal storage.
     * @param context
     * @param uniqueName
     * @return file
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !Environment.isExternalStorageRemovable() ? context.getExternalCacheDir().getPath() :
                        context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
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

    private Bitmap getBitmapFromDiskCache(String key) {
//        synchronized (mDiskCacheLock) {
//            // Wait while disk cache is started from background thread
//            while (mDiskCacheStarting) {
//                try {
//                    mDiskCacheLock.wait();
//                } catch (InterruptedException e) {}
//            }
//            if (diskCache != null) {
//                return diskCache.get(key);
//            }
//        }
        return null;
    }


    private void addBitmapToDiskCache(String url, Bitmap bitmap) {
//        synchronized (mDiskCacheLock) {
//            if (diskCache != null && diskCache.get(url) == null) {
//                diskCache.put(url, bitmap);
//            }
//        }
    }





}
