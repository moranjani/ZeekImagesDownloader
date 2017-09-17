package com.example.morana.zeekimages.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.morana.zeekimages.R;
import com.example.morana.zeekimages.Utility.Util;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Morana on 9/15/2017.
 */
public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>  {

    private ArrayList<String> urlPositionList;
    private DownloadOrganizer downloadOrganizer;


    public ImagesAdapter() {
        downloadOrganizer = new DownloadOrganizer();
        prepareDataSet();
    }



    private void prepareDataSet() {
        String[] urls = {Util.one , Util.two, Util.three, Util.four, Util.five, Util.six, Util.seven,
                Util.eight, Util.nine, Util.ten, Util.eleven, Util.twelve};
        urlPositionList = new ArrayList<>(Arrays.asList(urls));
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
        int height = Util.getScreenHeight(parent.getContext()) / 6;

        imageView.getLayoutParams().height = height;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.image.setImageResource(R.drawable.placeholder_image);
        String imageUrl = urlPositionList.get(position);
        downloadOrganizer.getImageIntoView(imageUrl, holder.image);
    }

    @Override
    public int getItemCount() {
        return urlPositionList.size();
    }

}
