package com.example.omgimbot.mapsbanking;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

//import com.mapbox.mapboxsdk.Mapbox;

/**
 * Created by fiyyanp on 2/1/2018.
 */

public class App extends Application {
    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }



    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        //Mapbox.getInstance(sApplication, getString(R.string.fake_key));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
