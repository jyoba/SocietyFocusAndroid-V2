package com.zircon.app.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.zircon.app.App;
import com.zircon.app.R;
import com.zircon.app.model.Complaint;
import com.zircon.app.ui.complaint.ComplaintsActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by jikoobaruah on 22/05/17.
 */

public class NotificationUtils {


    private static final int NOTIFY_COMPLAINTS = 87623;
    private static final String TAG = NotificationUtils.class.getSimpleName();

    public static void notifyComplaintRegistered(Complaint body) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(App.appInstance)
                        .setSmallIcon(R.drawable.ic_bug_report_white_transparent_72dp)
                        .setContentTitle("Complaint raised")
                        .setContentText(String.format("Your complaint with id : %s has been resigstered", body.complaintid));


        Intent resultIntent = new Intent(App.appInstance, ComplaintsActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        App.appInstance,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);


        NotificationManager notifyMgr =
                (NotificationManager) App.appInstance.getSystemService(NOTIFICATION_SERVICE);
        notifyMgr.notify(NOTIFY_COMPLAINTS, builder.build());


    }


//    public static void notifyPush(RemoteMessage remoteMessage) {
//        NotificationResponse notificationResponse = new NotificationResponse();
//        Log.e(TAG, "From: " + remoteMessage.getFrom());
//        Log.e(TAG, "Notification Message Body: " + remoteMessage.getData().get();
//        String mainPicUrl = remoteMessage.getData().get(notificationPanel.IARGS.IMAGE);
//        Bitmap image = null;
//        notification.setMain_picture();
//        notification.setTitle(remoteMessage.getData().get(notificationPanel.IARGS.TITLE));
//        notification.setMessage(remoteMessage.getData().get(notificationPanel.IARGS.BODY));
//        notification.setIcon(remoteMessage.getData().get(notificationPanel.IARGS.ICON));
//        URL url = null;
//        try {
//            url = new URL(notification.getMain_picture());
//            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            Log.e("Bitmap", " map: " + image.toString());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        android.support.v7.app.NotificationCompat.Builder mBuilder =
//                (android.support.v7.app.NotificationCompat.Builder) new android.support.v7.app.NotificationCompat.Builder(this);
//        Intent resultIntent;
//        if (!TextUtils.isEmpty(notification.getMain_picture())) {
//            android.support.v4.app.NotificationCompat.BigPictureStyle notiStyle = new android.support.v4.app.NotificationCompat.BigPictureStyle();
//            notiStyle.setSummaryText(notification.getMessage());
//            notiStyle.bigPicture(image);
//            mBuilder.setStyle(notiStyle);
//            notification.setIsnotify(true);
//            UserPasser.getInstance().setNotificationResponse(notification);
//            resultIntent = new Intent(this, SplashActivity.class);
//        } else {
//            resultIntent = new Intent(this, SplashActivity.class);
//            mBuilder.setStyle(new android.support.v7.app.NotificationCompat.BigTextStyle()
//                    .bigText(notification.getMessage()));
//        }
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        PendingIntent resultPendingIntent =
//                PendingIntent.getActivity(
//                        this,
//                        0,
//                        resultIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(notification.getTitle())
//                .setSound(defaultSoundUri)
//                .setContentIntent(resultPendingIntent)
//                .setContentText(notification.getMessage());
//        Random random = new Random();
//        int m = random.nextInt(9999 - 1000) + 1000;
//        NotificationManager mNotifyMgr =
//                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        mNotifyMgr.notify(m, mBuilder.build());
//
//    }

}
