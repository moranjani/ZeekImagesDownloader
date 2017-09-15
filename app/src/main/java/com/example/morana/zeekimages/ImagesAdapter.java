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

    ArrayList<String> urlPositionList;
    ArrayMap<String, Bitmap> urlsForDownload;


    public ImagesAdapter() {
        prepareDataSet();
    }



    private void prepareDataSet() {
        String[] urls = {UrlUtil.one , UrlUtil.two, UrlUtil.three, UrlUtil.four, UrlUtil.five, UrlUtil.six}; /* UrlUtil.seven,
                UrlUtil.eight, UrlUtil.nine, UrlUtil.ten, UrlUtil.eleven, UrlUtil.twelve};*/
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
        int height = UrlUtil.getScreenHeight(parent.getContext()) / 6;
        imageView.getLayoutParams().height = height;
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
