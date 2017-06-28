package com.zircon.app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jikoobaruah on 21/01/16.
 */
public class Society {

    public String name;

    public String societyId;

    public String address;

    public String contactDetail;

    public String societypic;

    @SerializedName("location_id")
    public Location location;

}