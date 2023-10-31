package activity.utility;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;


import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;


public class AppController extends Application {

    private static AppController mInstance;

    private static Context mContext;


    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private ImageLoader mImageLoader;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = this;
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

    }



    public static Context getContext() {
        return mContext;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }
    //    public ImageLoader getImageLoader() {
    //        getRequestQueue();
    //        if (mImageLoader == null) {
    //            mImageLoader = new ImageLoader(this.mRequestQueue,
    //                    new LruBitmapCache());
    //        }
    //        return this.mImageLoader;
    //    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}

