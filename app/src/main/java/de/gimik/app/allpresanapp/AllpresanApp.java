package de.gimik.app.allpresanapp;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class AllpresanApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/BosisStd-Light.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .build());
    }

}
