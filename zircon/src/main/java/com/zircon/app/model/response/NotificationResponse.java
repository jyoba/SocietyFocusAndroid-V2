package com.zircon.app.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jikoobaruah on 15/05/17.
 */

public class NotificationResponse {


    private boolean isnotify;

    @SerializedName("Icon")
    private String icon;

    @SerializedName("Message")
    private String message;

    @SerializedName("Title")
    private String title;

    @SerializedName("main_picture")
    private String mainPicture;

    public boolean isnotify() {
        return isnotify;
    }

    public void setIsnotify(boolean isnotify) {
        this.isnotify = isnotify;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(String mainPicture) {
        this.mainPicture = mainPicture;
    }
}
