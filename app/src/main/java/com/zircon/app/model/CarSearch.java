package com.zircon.app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jikoobaruah on 10/05/17.
 */

public class CarSearch {

    @SerializedName("vehicleid")
    public int VehicleId;

    public User user;

    @SerializedName("vehiclenumber")
    public String VehicleNumber;

    @SerializedName("type")
    public int Type;

    @SerializedName("status")
    public int Status;

    @SerializedName("parkingslot")
    public String ParkingSlot;
}
