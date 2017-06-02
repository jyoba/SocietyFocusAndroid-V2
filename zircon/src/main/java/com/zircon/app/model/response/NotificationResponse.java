package com.zircon.app.model.response;

import android.text.TextUtils;

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by jikoobaruah on 15/05/17.
 */

public class NotificationResponse {


    public boolean isnotify;

    @SerializedName("Icon")
    public String icon;

    @SerializedName("Message")
    public String message;

    @SerializedName("Title")
    public String title;

    @SerializedName("main_picture")
    public String mainPicture;

    public static NotificationResponse getFromRemoteMessage(RemoteMessage remoteMessage) throws IllegalAccessException {
        NotificationResponse response = null;
        String data = null;

        Field[] fields = NotificationResponse.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getDeclaredAnnotations() != null && field.getDeclaredAnnotations().length > 0) {
                data = remoteMessage.getData().get(field.getDeclaredAnnotations()[0].toString());
            } else {
                data = remoteMessage.getData().get(field.getName());
            }
            if (!TextUtils.isEmpty(data)) {
                if (response == null) response = new NotificationResponse();
                field.set(response, data);
            }
        }

        response.isnotify = true;

        return response;
    }


}
