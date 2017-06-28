package com.zircon.app.ui.pushNotification;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.zircon.app.utils.AccountManager;
import com.zircon.app.utils.HTTP;

/**
 * Created by jikoobaruah on 15/05/17.
 */

public class InstanceIdService extends FirebaseInstanceIdService {

    final String TAG = InstanceIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "onTokenRefresh " + refreshedToken);
        if (AccountManager.getInstance().getloggedInUser() != null) {
            HTTP.getAPI().sendFcmToken(AccountManager.getInstance().getToken(), refreshedToken).enqueue(null);
        }
    }
}
