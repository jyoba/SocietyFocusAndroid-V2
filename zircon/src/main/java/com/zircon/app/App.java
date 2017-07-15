package com.zircon.app;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.zircon.app.model.Emergency;

/**
 * Created by jikoobaruah on 07/04/17.
 */

public class App extends Application {

    public static App appInstance;

    static String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;


        setupFireBaseRemoteConfig();
    }

    private void setupFireBaseRemoteConfig() {

        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setDefaults(R.xml.remote_configs_default);
        firebaseRemoteConfig.fetch(60) // fetch every minutes
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "remote config is fetched.");
                            firebaseRemoteConfig.activateFetched();
                        }
                    }
                });
    }
}
