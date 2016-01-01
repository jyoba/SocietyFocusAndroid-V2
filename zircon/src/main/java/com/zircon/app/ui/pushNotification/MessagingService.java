package com.zircon.app.ui.pushNotification;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.zircon.app.model.response.NotificationResponse;

/**
 * Created by jikoobaruah on 15/05/17.
 */

public class MessagingService extends FirebaseMessagingService {

    final String TAG = FirebaseMessagingService.class.getSimpleName();
    private NotificationResponse notification;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG,"onMessageReceived "+remoteMessage.getData());



    }
}
