package com.example.morana.zeekimages.download;

import android.graphics.Bitmap;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Morana on 9/14/2017.
 */
public class ImageFileDownloader {
    private static final String TAG = ImageFileDownloader.class.getSimpleName();

    private static ImageFileDownloader INSTANCE = new ImageFileDownloader();
    private ExecutorService executor;
    private ArrayMap<String, Bitmap> imagesCache;
    private ArrayMap<String, DownloadTransaction> downloadingMap;
    private Object cacheLock = new Object();

    private ImageFileDownloader() {
        executor = Executors.newCachedThreadPool();
        imagesCache = new ArrayMap<>();
        downloadingMap = new ArrayMap<>();
    }

    public static ImageFileDownloader getInstance(){
        return INSTANCE;
    }


    public void getBitmapForUrl(String urlString, WeakReference<TransactionListener> listenerRef) {
        if (listenerRef == null) return;
        TransactionListener listener = listenerRef.get();
        if (listener == null) return;

        Bitmap bitmap = fetchImageFromCache(urlString);
        if (bitmap != null) {
            Log.d(TAG, "BAM! had it in cache!");
            listener.onBitmapDownloaded(urlString, bitmap);
        } else {
            Log.d(TAG, "Url was not found in cache. Will notify once downloaded..");
            downloadBitmap(urlString, listenerRef);
        }
    }




    private synchronized void downloadBitmap(final String urlString, final WeakReference<TransactionListener> listener) {
        if (isDownloadingTransaction(urlString)) {
            addListenerToTransaction(urlString, listener);
            return;
        }

        DownloadTransaction transaction = new DownloadTransaction(urlString, listener);
        downloadingMap.put(urlString, transaction);
        executor.submit(transaction);
    }

    private void addListenerToTransaction(String urlString, WeakReference<TransactionListener> listener) {
        DownloadTransaction transaction = getTransaction(urlString);
        if (transaction != null) {
            transaction.addListener(listener);
        }
    }

    private synchronized DownloadTransaction getTransaction(String url) {
        return downloadingMap.get(url);
    }

    private synchronized boolean isDownloadingTransaction(String url) {
        return (downloadingMap.containsKey(url));
    }

    public synchronized void removeFinishedTransaction(String url) {
        downloadingMap.remove(url);
    }


    // CACHE OPERATIONS

    private Bitmap fetchImageFromCache(String urlString) {
        synchronized (cacheLock) {
            return imagesCache.get(urlString);
        }
    }

    public void addDownloadedImageToCache(String url, Bitmap bitmap) {
        synchronized(cacheLock) {
            imagesCache.put(url, bitmap);
        }
    }


    public interface TransactionListener {
        public void onBitmapDownloaded(String urlString, Bitmap bitmap);
        public void onError(String urlString);
    }
}
