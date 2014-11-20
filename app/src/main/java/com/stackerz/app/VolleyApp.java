package com.stackerz.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by ed on 20/11/14.
 */
public class VolleyApp extends Application {

    private static VolleyApp mInstance;
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        this.setAppContext(getApplicationContext());
    }

    public static VolleyApp getInstance(){
        return mInstance;
    }
    public static Context getAppContext() {
        return mAppContext;
    }
    public void setAppContext(Context mAppContext) {
        this.mAppContext = mAppContext;
    }
}

