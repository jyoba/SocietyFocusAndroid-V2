package com.zircon.app.utils.ui;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.zircon.app.ui.home.HomeActivity;

/**
 * Created by jikoobaruah on 14/05/17.
 */

public class ForceUpdateHelper {

    public static final String KEY_UPDATE_REQUIRED = "force_update_required";
    public static final String KEY_CURRENT_VERSION = "force_update_current_version";
    public static final String KEY_UPDATE_URL = "force_update_store_url";

    public static void checkForUpdates(Context context) {
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        int updateVersion = Integer.parseInt(remoteConfig.getString(KEY_CURRENT_VERSION));
        int appVersion = getAppVersion(context);
        boolean forceUpdateNeeded = remoteConfig.getBoolean(KEY_UPDATE_REQUIRED);

        if (updateVersion > appVersion && forceUpdateNeeded) {
            showForceUpdateDialog(context);
        }else if (updateVersion > appVersion){
            showUpdateDialog(context);
        }
    }

    private static void showForceUpdateDialog(Context context) {
    }

    private static void showUpdateDialog(Context context) {
        
    }

    private static int getAppVersion(Context context) {
        int result = -1;

        try {
            result = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }
}
