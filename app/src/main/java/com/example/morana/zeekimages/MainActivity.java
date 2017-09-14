package com.example.morana.zeekimages;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.lang.ref.WeakReference;


public class MainActivity extends Activity implements ImageFileDownloader.Listener {

    private static final String urlString = "http://www.directexpose.com/wp-content/uploads/2017/03/The_23_Cutest_Dog_Breeds_Youve_Never_Even_Heard_Of_3223_5026-e1488937090793.jpg";
    ImageFileDownloader imageFileDownloader;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       imageFileDownloader = new ImageFileDownloader();
       imageView = findViewById(R.id.my_image);
       imageFileDownloader.getBitmapForUrl(urlString, new WeakReference<ImageFileDownloader.Listener>(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBitmapDownloaded(String urlString, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
