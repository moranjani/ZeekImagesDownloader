package com.example.morana.zeekimages.download;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * Created by Morana on 9/16/2017.
 */
public class DiskCache {





    private void savePicture(String filename, Bitmap bitmap, Context context){
        try {
            ObjectOutputStream outputStream;
            FileOutputStream out;
            out = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream = new ObjectOutputStream(out);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            outputStream.close();
            outputStream.notifyAll();
            out.notifyAll();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap loadPicture(String filename, Bitmap bitmap, Context context){
        try {
            FileInputStream fileInputStream = context.openFileInput(filename);
            ObjectInputStream objectInputStream = null;
            try {
                objectInputStream = new ObjectInputStream(fileInputStream);
            } catch (StreamCorruptedException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            bitmap = BitmapFactory.decodeStream(objectInputStream);
            try {
                objectInputStream.close();
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;

    }



    // Creates a unique subdirectory of the designated app cache directory. Tries to use external
    // but if not mounted, falls back on internal storage.
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
