package com.zircon.app;

import android.app.Application;

/**
 * Created by jikoobaruah on 07/04/17.
 */

public class App extends Application {

    public static App appInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
    }
}
