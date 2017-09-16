package com.example.morana.zeekimages.download;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

import static com.example.morana.zeekimages.download.ImageFileDownloader.TransactionListener;

/**
 * Created by Morana on 9/15/2017.
 */
public class DownloadTransaction implements Runnable {


    private static final String TAG = DownloadTransaction.class.getSimpleName();
    private String urlForDownload;
    private HashSet<WeakReference<TransactionListener>> listeners;

    public DownloadTransaction(String url, WeakReference<TransactionListener> listener) {
        urlForDownload = url;
        listeners = new HashSet<>();
        addListener(listener);
    }


    @Override
    public void run() {
        Bitmap bitmap = null;
        bitmap = getBitmapFromURL(urlForDownload);

        ImageFileDownloader.getInstance().addDownloadedImageToCache(urlForDownload, bitmap);
        notifyListeners(bitmap);
        ImageFileDownloader.getInstance().removeFinishedTransaction(urlForDownload);
    }

    public synchronized void addListener(WeakReference<TransactionListener> listener) {
        listeners.add(listener);
    }

    private synchronized void notifyListeners(final Bitmap bitmap) {
        if (listeners.isEmpty()) {
            return;
        }

        if (bitmap == null) {
            for (WeakReference<TransactionListener> listenerRef : listeners) {
                TransactionListener transactionListener = listenerRef.get();
                if (transactionListener != null) {
                    notifyError(urlForDownload, transactionListener);
                }
            }
        } else {
            for (WeakReference<TransactionListener> listenerRef : listeners) {
                TransactionListener transactionListener = listenerRef.get();
                if (transactionListener != null) {
                    notifySuccess(urlForDownload, transactionListener, bitmap);
                }
            }
        }
    }


    private void notifyError(final String url, final TransactionListener listener) {
        Runnable action = new Runnable() {
            @Override
            public void run() {
                listener.onError(urlForDownload);
            }
        };
        runOnUiThread(action);
    }

    private void notifySuccess(final String url, final TransactionListener listener, final Bitmap bitmap) {
        Runnable action = new Runnable() {
            @Override
            public void run() {
                listener.onBitmapDownloaded(url, bitmap);
            }
        };
        runOnUiThread(action);
    }



    private void runOnUiThread(Runnable action) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            action.run();
            return;
        }

        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(action);
    }


    private Bitmap getBitmapFromURL(String src) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        Bitmap bitmap = null;

        try {
            URL url = new URL(src);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            Log.e(TAG, "getBitmapFromURL:", e);
            return null;
        } catch (Exception e) {
            Log.e(TAG, "getBitmapFromURL:", e);
            return null;
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }catch (Exception e) {
                    Log.e(TAG, "getBitmapFromURL:", e);
                    return null;
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return bitmap;
    }


}
