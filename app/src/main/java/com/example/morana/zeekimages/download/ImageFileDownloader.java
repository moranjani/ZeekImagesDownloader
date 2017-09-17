package com.example.morana.zeekimages.download;

import android.content.Context;
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
    private Context context;
    private ExecutorService executor;
    private ArrayMap<String, DownloadTransaction> downloadingMap;
    private ImagesCache cache;
    private Object cacheLock = new Object();

    private ImageFileDownloader() {
        executor = Executors.newCachedThreadPool();
        downloadingMap = new ArrayMap<>();
        cache = new ImagesCache(context);
    }

    public static ImageFileDownloader getInstance(){
        return INSTANCE;
    }

    public void init(Context context) {
        this.context = context;
    }


    /**
     * Returns a bitmap that corresponds to the urlString, either from the cache or downloaded from web
     * @param urlString
     * @param listenerRef
     */
    public void getBitmapForUrl(String urlString, WeakReference<TransactionListener> listenerRef) {
        if (listenerRef == null) return;
        TransactionListener listener = listenerRef.get();
        if (listener == null) return;

        Bitmap bitmap = cache.getBitmapFromCache(urlString);
        if (bitmap != null) {
            Log.d(TAG, "DONE! had it in cache!");
            listener.onBitmapDownloaded(urlString, bitmap);
        } else {
            Log.d(TAG, "DOWNLOADING... Url was not found in cache. Will notify once downloaded..");
            downloadBitmap(urlString, listenerRef);
        }
    }

    public void addDownloadedImageToCache(String urlForDownload, Bitmap bitmap) {
        cache.addBitmapToCache(urlForDownload, bitmap);
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




    public interface TransactionListener {
        public void onBitmapDownloaded(String urlString, Bitmap bitmap);
        public void onError(String urlString);
    }
}
