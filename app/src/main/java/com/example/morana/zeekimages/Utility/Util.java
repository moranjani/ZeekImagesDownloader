package com.example.morana.zeekimages.Utility;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Morana on 9/15/2017.
 */
public class Util {
    public static final String one = "https://previews.123rf.com/images/hydognik/hydognik1105/hydognik110500008/9483336-Metal-Number-1-with-a-silvery-fringing-on-a-red-background-Stock-Photo.jpg";
    public static final String two = "http://clipart-library.com/data_images/356147.png";
    public static final String three = "http://media.gizmodo.co.uk/wp-content/uploads/2013/02/Three-4G.jpg";
    public static final String four = "http://clipart-library.com/data_images/38719.png";
    public static final String five = "https://ak3.picdn.net/shutterstock/videos/5243/thumb/1.jpg?i10c=img.resize(height:160)";
    public static final String six = "http://clipart-library.com/image_gallery/422843.png";
    public static final String seven = "https://vignette2.wikia.nocookie.net/spongefan/images/b/b9/Hand-7-seven.jpg/revision/latest?cb=20140323150027";
    public static final String eight = "https://openclipart.org/image/2400px/svg_to_png/71137/eight.png";
    public static final String nine = "http://cdn.images.express.co.uk/img/dynamic/109/590x/Nine-603820.jpg";
    public static final String ten = "https://upload.wikimedia.org/wikipedia/commons/a/a5/TWHW10.png";
    public static final String eleven = "http://www.clker.com/cliparts/m/W/j/b/w/O/candy-11-hi.png";
    public static final String twelve = "https://i.ytimg.com/vi/wIIi38dHjos/maxresdefault.jpg";





    public static int getScreenHeight(Context context) {
        int screenHeight = 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenHeight = size.y;
        return screenHeight;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


}
