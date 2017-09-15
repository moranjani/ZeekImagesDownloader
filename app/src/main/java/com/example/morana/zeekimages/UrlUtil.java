package com.example.morana.zeekimages;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Morana on 9/15/2017.
 */
public class UrlUtil {
    public static final String one = "https://previews.123rf.com/images/hydognik/hydognik1105/hydognik110500008/9483336-Metal-Number-1-with-a-silvery-fringing-on-a-red-background-Stock-Photo.jpg";
    public static final String two ="http://www.allfree-clipart.com/Food/two-cherries.jpg";
    public static final String three ="http://media.gizmodo.co.uk/wp-content/uploads/2013/02/Three-4G.jpg";
    public static final String four ="http://online2.byu.edu/wp-content/uploads/2013/05/number-four-500x295.jpg";
    public static final String five ="https://ak3.picdn.net/shutterstock/videos/5243/thumb/1.jpg?i10c=img.resize(height:160)";
    public static final String six ="http://www.privacyanddatasecurityinsight.com/files/2015/09/Six.jpg";
    public static final String seven ="https://www.google.co.il/search?q=seven&source=lnms&tbm=isch&sa=X&ved=0ahUKEwju86uSu6fWAhWHfRoKHWk4CuwQ_AUICigB&biw=1366&bih=589#imgdii=wFLvmSx4Oag7GM:&imgrc=OXvPdUyPAbS6NM:";
    public static final String eight ="https://www.google.co.il/search?q=eight&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjy_4K6u6fWAhXDyRoKHZAMB-cQ_AUICigB&biw=1366&bih=589#imgrc=-KCXmVXROBED-M:";
    public static final String nine ="https://www.google.co.il/search?q=nine&tbm=isch&source=lnt&tbs=isz:m&sa=X&ved=0ahUKEwi5m6DSu6fWAhUBLhoKHR2CC-8QpwUIHQ&biw=1366&bih=589&dpr=1#imgrc=8ePWxAXq-x_Y0M:";
    public static final String ten ="https://www.google.co.il/search?q=10+image&source=lnms&tbm=isch&sa=X&ved=0ahUKEwinkNzku6fWAhWFAxoKHfr2AugQ_AUICigB&biw=1366&bih=589#imgrc=dKsjTqOODBD5eM:";
    public static final String eleven ="https://www.google.co.il/search?q=11+image&source=lnms&tbm=isch&sa=X&ved=0ahUKEwj3-uPyu6fWAhUI5xoKHafaCecQ_AUICigB&biw=1366&bih=589#imgrc=lAndt6RqjbphbM:";
    public static final String twelve ="https://www.google.co.il/search?q=twelve&source=lnms&tbm=isch&sa=X&ved=0ahUKEwi2nsGevKfWAhWHhRoKHeFSCeoQ_AUICigB&biw=1366&bih=589#imgrc=HG5EVWbj8PrPIM:";



    public static int getScreenHeight(Context context) {
        int screenHeight=0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenHeight = size.y;
        return screenHeight;
    }
}
