package com.example.morana.zeekimages.download;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by Morana on 9/16/2017.
 */
public class DiskCache {

    private static final String DISK_CACHE_SUBDIR = "thumbnails";
    private static final String TAG = DiskCache.class.getSimpleName();

    private Context context;
    File cacheDir;
    private ArrayMap<String, File> fileMap;
    private ArrayList<String> orderedFileList;
    private ExecutorService executor;

    private Runnable initAction = new Runnable() {
        @Override
        public void run() {
            openDirectory();
        }
    };


    public DiskCache(Context context, ExecutorService executor) {
        this.context = context;
        this.executor = executor;
        fileMap = new ArrayMap<>();
        orderedFileList = new ArrayList<>();

        // Initialize disk cache on background thread
        executor.execute(initAction);
    }



    public synchronized void saveBitmapToDiskCache(final String url, final Bitmap bitmap) {
        if (fileMap.get(url) == null) {
            if (isCacheTooFullForInserting(bitmap)) {
                deleteOldItemsFromCache(bitmap);
            }
            File file = new File(cacheDir.getPath() + File.separator + "file" + (fileMap.size() + 1) + ".png");
            final String filePath = file.getPath();
            fileMap.put(url, file);
            orderedFileList.add(url);

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    savePicture(filePath, bitmap);
                }
            });
        }
    }

    private void deleteOldItemsFromCache(Bitmap newBitmap) {
        // get the size of the new bitmap, compute the sum of the sizes of the bitmaps in the orderedFileList one by one,
        // once the sum is bigger the the size of the newBitmap, delete those first bitmap in the list.

    }

    private boolean isCacheTooFullForInserting(Bitmap bitmap) {
       // int byteCount = bitmap.getAllocationByteCount();
        //..
        // if ((currentCacheSize + byteCount) > MAX_CACHE_SIZE {
        // return true;
        return false;
    }

    private void savePicture(final String filename, Bitmap bitmap){

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "", e);
            }
        }
    }


    public synchronized Bitmap getBitmapFromDisk(String url) {
        final File file = fileMap.get(url);
        if (file == null) {
            Log.d(TAG, "file was not found in the disk cache");
            return null;
        }

        Callable<Bitmap> callable = new Callable<Bitmap>() {
            @Override
            public Bitmap call() throws Exception {
                return loadPicture(file.getPath());
            }
        };

        Future<Bitmap> future = executor.submit(callable);
        if (future != null) {
            try {
                return future.get();
            } catch (InterruptedException e) {
                Log.e(TAG, "", e);
                return null;
            } catch (ExecutionException e) {
                Log.e(TAG, "", e);
                return null;
            }
        }
        return null;
    }



    private Bitmap loadPicture(String filename){
        Bitmap bitmap = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(filename);
            bitmap = BitmapFactory.decodeStream(fileInputStream);

            try {
                fileInputStream.close();
            } catch (IOException e) {
                Log.e(TAG, "", e);
            }

        } catch (FileNotFoundException e) {
            Log.e(TAG, "", e);
        }
        return bitmap;

    }


    private synchronized void openDirectory() {
        cacheDir = getDiskCacheDir(this.context, DISK_CACHE_SUBDIR);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
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
}
