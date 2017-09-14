package com.example.morana.zeekimages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Morana on 9/14/2017.
 */
public class ImageFileDownloader {
    private static final String TAG = ImageFileDownloader.class.getName();

    private static ImageFileDownloader INSTANCE = new ImageFileDownloader();

    private ImageFileDownloader() {
        executor = Executors.newCachedThreadPool();
        imagesMap = new HashMap<>();
    }

    public static ImageFileDownloader getInstance(){
        return INSTANCE;
    }


    ExecutorService executor;
    HashMap<String, Bitmap> imagesMap;


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


    private Bitmap downloadBitmap(final String urlString, WeakReference<Listener> listener) {
        Bitmap bitmap = null;

        Callable callable = new Callable<Bitmap>() {
            @Override
            public Bitmap call() {
                return getBitmapFromURL(urlString);
            }
        };
        Future<Bitmap> future = executor.submit(callable);
        try {
            // get() waits for the task to finish and then gets the result
            bitmap = future.get();
            Log.d(TAG, new StringBuilder().append("got the bitmap for: ").append(urlString).toString());
            imagesMap.put(urlString, bitmap);
            notifyListener(listener, urlString, bitmap);
        } catch (InterruptedException e) {
            // thrown if task was interrupted before completion
            e.printStackTrace();
            // thrown if task was interrupted before completion
            e.printStackTrace();
        } catch (ExecutionException e) {
            // thrown if the task threw an execption while executing
            e.printStackTrace();
        } finally {
            return bitmap;
        }
    }

    public void getBitmapForUrl(String urlString, WeakReference<Listener> listener) {
        Bitmap bitmap = imagesMap.get(urlString);
        if (bitmap != null) {
            Log.d(TAG, "BAMM have it in my cache!!!");
            notifyListener(listener, urlString, bitmap);
        } else {
            downloadBitmap(urlString, listener);
        }
    }

    private void notifyListener(WeakReference<Listener> listenerRf, String urlString, Bitmap bitmap) {
        Listener listener = listenerRf.get();
        if (listener != null) {
            listener.onBitmapDownloaded(urlString, bitmap);
        }
    }


    public interface Listener {
        public void onBitmapDownloaded(String urlString, Bitmap bitmap);
    }
}
