package com.example.morana.zeekimages;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.lang.ref.WeakReference;


public class MainActivity extends AppCompatActivity implements ImageFileDownloader.Listener {

    private static final String puppyUrl = "http://www.directexpose.com/wp-content/uploads/2017/03/The_23_Cutest_Dog_Breeds_Youve_Never_Even_Heard_Of_3223_5026-e1488937090793.jpg";
    private static final String flowerUrl = "https://static.pexels.com/photos/36764/marguerite-daisy-beautiful-beauty.jpg";

    ImageView imageView;
    ImageView flowerImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.my_image);
        flowerImageView = (ImageView) findViewById(R.id.flower);
        ImageFileDownloader.getInstance().getBitmapForUrl(puppyUrl, new WeakReference<ImageFileDownloader.Listener>(this));
        ImageFileDownloader.getInstance().getBitmapForUrl(flowerUrl, new WeakReference<ImageFileDownloader.Listener>(this));
    }



    @Override
    public void onBitmapDownloaded(String urlString, Bitmap bitmap) {
        switch (urlString) {
            case puppyUrl: {
                imageView.setImageBitmap(bitmap);
                break;
            }
            case flowerUrl: {
                flowerImageView.setImageBitmap(bitmap);
                break;
            }
        }

    }
}
