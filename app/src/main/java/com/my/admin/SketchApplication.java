package com.my.admin;

import android.app.Application;

import cat.ereza.customactivityoncrash.config.CaocConfig;

public class SketchApplication extends Application {

    public void onCreate() {
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM)
                .trackActivities(true)
                .errorDrawable(R.drawable.app_icon)
                .restartActivity(MainActivity.class)
                .apply();
        super.onCreate();
    }
}
