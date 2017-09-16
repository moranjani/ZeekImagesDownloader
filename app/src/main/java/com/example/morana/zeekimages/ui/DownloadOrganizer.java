package com.example.morana.zeekimages.ui;

import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Morana on 9/15/2017.
 */
public class DownloadOrganizer  {

    private static ArrayList<ImageRequest> requests = new ArrayList<>();

    public static void getImageIntoView(String imageUrl, ImageView image) {
        ImageRequest request = new ImageRequest(imageUrl, new WeakReference<ImageView>(image));
        requests.add(request);
        request.fetch();
    }

    public static void remove(ImageRequest request) {
        requests.remove(request);
    }
}
