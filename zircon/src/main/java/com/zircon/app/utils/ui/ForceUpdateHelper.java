package com.zircon.app.utils.ui;

import android.content.Context;
import android.content.pm.PackageManager;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

/**
 * Created by jikoobaruah on 14/05/17.
 */

public class ForceUpdateHelper {

    public static final String KEY_UPDATE_REQUIRED = "force_update_required";
    public static final String KEY_CURRENT_VERSION = "force_update_current_version";

    public static int checkUpdateStatus(Context context) {
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        int updateVersion = Integer.parseInt(remoteConfig.getString(KEY_CURRENT_VERSION));
        int appVersion = getAppVersion(context);
        boolean forceUpdateNeeded = remoteConfig.getBoolean(KEY_UPDATE_REQUIRED);

        if (updateVersion > appVersion && forceUpdateNeeded) {
            return UpdateType.FORCED_UPDATE;
        } else if (updateVersion > appVersion) {
            return UpdateType.OPTIONAL_UPDATE;
        } else
            return UpdateType.NO_UPDATE;
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


    public interface UpdateType {
        int NO_UPDATE = 0;
        int OPTIONAL_UPDATE = 1;
        int FORCED_UPDATE = 2;
    }
}
