package com.example.morana.zeekimages;

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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Morana on 9/14/2017.
 */
public class ImageFileDownloader {
    private static final String TAG = ImageFileDownloader.class.getName();

    private static ImageFileDownloader INSTANCE = new ImageFileDownloader();
    private Handler uiHandler;

    private ImageFileDownloader() {
        executor = Executors.newCachedThreadPool();
        imagesMap = new ConcurrentHashMap<>();
    }

    public static ImageFileDownloader getInstance(){
        return INSTANCE;
    }


    ExecutorService executor;
    ConcurrentHashMap<String, Bitmap> imagesMap;


    private static Bitmap getBitmapFromURL(String src) {
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


    private void downloadBitmap(final String urlString, final WeakReference<Listener> listener) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                bitmap = getBitmapFromURL(urlString);
                if (bitmap!=null) {
                    Log.d(TAG, new StringBuilder().append("got the bitmap for: ").append(urlString).toString());
                    imagesMap.put(urlString, bitmap);
                    notifyListener(listener, urlString, bitmap);
                }
            }
        };

        executor.submit(runnable);
    }

    public void getBitmapForUrl(String urlString, WeakReference<Listener> listener) {
        Bitmap bitmap = imagesMap.get(urlString);
        if (bitmap != null) {
            Log.d(TAG, "BAMM had it in my cache!!!");
            notifyListener(listener, urlString, bitmap);
        } else {
            downloadBitmap(urlString, listener);
        }
    }

    private void notifyListener(WeakReference<Listener> listenerRf, String urlString, Bitmap bitmap) {
        Listener listener = listenerRf.get();
        if (listener != null) {
            notifyOnUiThread(urlString, bitmap, listener);
        }
    }

    private void notifyOnUiThread(final String urlString, final Bitmap bitmap, final Listener listener) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listener.onBitmapDownloaded(urlString, bitmap);
            }
        });
    }

    private void runOnUiThread(Runnable action) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            action.run();
            return;
        }

        if (uiHandler == null) {
            uiHandler = new Handler(Looper.getMainLooper());
        }
        uiHandler.post(action);

    }




    public interface Listener {
        public void onBitmapDownloaded(String urlString, Bitmap bitmap);
    }
}
