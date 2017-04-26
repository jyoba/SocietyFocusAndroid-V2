package com.zircon.app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jikoobaruah on 08/04/17.
 */

public class NoticeBoard {

    public int id;

    @SerializedName("image_url2")
    public String imageUrl2;

    public String title;

    @SerializedName("image_url1")
    public String imageUrl1;

    public int status;

    @SerializedName("modifiedate")
    public String modifieDate;

    public String description;

    public String isPinned;

    public int score;

    @SerializedName("creationdate")
    public String creationDate;

    public User user;

    
}