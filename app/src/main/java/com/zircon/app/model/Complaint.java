package com.zircon.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jikoobaruah on 03/05/17.
 */

public class Complaint implements Parcelable{

    public String complaintid;
    public String societyId;
    public String title;
    public String description;

    public String creationdate;

    public String status;

    public boolean isSynced = true;

    public Complaint(){}

    protected Complaint(Parcel in) {
        complaintid = in.readString();
        societyId = in.readString();
        title = in.readString();
        description = in.readString();
        creationdate = in.readString();
        status = in.readString();
        isSynced = in.readByte() != 0;
    }

    public static final Creator<Complaint> CREATOR = new Creator<Complaint>() {
        @Override
        public Complaint createFromParcel(Parcel in) {
            return new Complaint(in);
        }

        @Override
        public Complaint[] newArray(int size) {
            return new Complaint[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(complaintid);
        dest.writeString(societyId);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(creationdate);
        dest.writeString(status);
        dest.writeByte((byte) (isSynced ? 1 : 0));
    }
}
