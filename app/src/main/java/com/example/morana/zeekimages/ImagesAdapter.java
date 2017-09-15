package com.example.morana.zeekimages;

import android.graphics.Bitmap;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Morana on 9/15/2017.
 */
public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageViewHolder> implements ImageFileDownloader.Listener {

    private static final String puppyUrl = "http://www.directexpose.com/wp-content/uploads/2017/03/The_23_Cutest_Dog_Breeds_Youve_Never_Even_Heard_Of_3223_5026-e1488937090793.jpg";
    private static final String flowerUrl = "https://static.pexels.com/photos/36764/marguerite-daisy-beautiful-beauty.jpg";

    ArrayList<String> urlPositionList;
    ArrayMap<String, Bitmap> urlsForDownload;


    public ImagesAdapter() {
        prepareDataSet();
    }



    private void prepareDataSet() {
        String[] urls = {puppyUrl, flowerUrl, flowerUrl, flowerUrl, puppyUrl, puppyUrl, puppyUrl, flowerUrl};
        urlPositionList = new ArrayList<>(Arrays.asList(urls));
        urlsForDownload = new ArrayMap<>();
        for (String url : urls) {
            urlsForDownload.put(url, null);
        }
        for (String url : urlsForDownload.keySet()) {
            ImageFileDownloader.getInstance().getBitmapForUrl(url, new WeakReference<ImageFileDownloader.Listener>(this));
        }
    }

    @Override
    public void onBitmapDownloaded(String downloadedUrl, Bitmap bitmap) {
        if (urlsForDownload.containsKey(downloadedUrl)) {
            urlsForDownload.put(downloadedUrl, bitmap);
        }
        notifyDataSetChanged();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ImageViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image_item_view);
        }

    }


    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View imageView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);

        ImageViewHolder viewHolder = new ImageViewHolder(imageView);
        int height = parent.getMeasuredWidth() / 4;
        imageView.setMinimumHeight(height);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        String imageUrl = urlPositionList.get(position);
        Bitmap bitmap = urlsForDownload.get(imageUrl);
        if (bitmap!=null) {
            holder.image.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return urlPositionList.size();
    }

}
