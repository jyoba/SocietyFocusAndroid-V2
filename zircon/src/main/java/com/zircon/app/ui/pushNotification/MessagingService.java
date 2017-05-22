package com.zircon.app.ui.pushNotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.zircon.app.R;
import com.zircon.app.model.response.NotificationResponse;
import com.zircon.app.ui.login.SplashActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

/**
 * Created by jikoobaruah on 15/05/17.
 */

public class MessagingService extends FirebaseMessagingService {

    final String TAG = FirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e(TAG, "From: " + remoteMessage.getFrom());

        try {
            NotificationResponse notification = NotificationResponse.getFromRemoteMessage(remoteMessage);
            URL url = new URL(notification.mainPicture);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            Log.e("Bitmap", " map: " + image.toString());

            NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this);


            Intent resultIntent;
            if (!TextUtils.isEmpty(notification.mainPicture)) {
                android.support.v4.app.NotificationCompat.BigPictureStyle notiStyle = new android.support.v4.app.NotificationCompat.BigPictureStyle();
                notiStyle.setSummaryText(notification.message);
                notiStyle.bigPicture(image);
                mBuilder.setStyle(notiStyle);
                notification.isnotify = true;
                resultIntent = new Intent(this, SplashActivity.class);
            } else {
                resultIntent = new Intent(this, SplashActivity.class);
                mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(notification.message));
            }

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(notification.title)
                    .setSound(defaultSoundUri)
                    .setContentIntent(resultPendingIntent)
                    .setContentText(notification.message);
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(m, mBuilder.build());


        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
