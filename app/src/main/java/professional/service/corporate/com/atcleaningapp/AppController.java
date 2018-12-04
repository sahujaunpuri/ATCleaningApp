package professional.service.corporate.com.atcleaningapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context context;
    private static AppController mInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        AppController.context = getApplicationContext();

//        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//
//            @Override
//
//            public void uncaughtException(Thread thread, Throwable ex) {
//
//                handleUncaughtException(thread, ex);
//
//            }
//
//        });
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
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
    public static Context getAppContext() {
        return AppController.context;
    }
    public void handleUncaughtException (Thread thread, Throwable e)

    {

        String stackTrace = Log.getStackTraceString(e);

        String message = e.getMessage();

        Intent intent = new Intent (Intent.ACTION_SEND);

        intent.setType("message/rfc822");

        intent.putExtra (Intent.EXTRA_EMAIL, new String[] {"itsmesourav.freelancer@gmail.com"});

        intent.putExtra (Intent.EXTRA_SUBJECT, "NavigationBarContractors Crash Report");

        intent.putExtra (Intent.EXTRA_TEXT, stackTrace);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application

        startActivity(intent);

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}