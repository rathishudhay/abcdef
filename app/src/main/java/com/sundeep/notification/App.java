package com.sundeep.notification;

import android.app.Application;
import android.content.Context;
import android.util.Log;

//import com.sundeep.Rado_Whatsapp_Toolkit.Addons.InstaDownloader.prefs.PreferencesManager;

/**
 * @author yarolegovich https://github.com/yarolegovich
 * 25.02.2017.
 */

public class App extends Application {

    private static App instance;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("APP_INSTANCE", "onCreate: ");
        instance = this;
        mContext = getApplicationContext();
    }

    public static App getInstance() {
        return instance;
    }
    public static Context getAppContext() {
        return mContext;
    }
}
