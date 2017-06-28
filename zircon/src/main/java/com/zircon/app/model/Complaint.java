package com.zircon.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by jikoobaruah on 03/05/17.
 */

public class Complaint implements Parcelable {

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
    public String complaintid;
    public String societyId;
    public String title;
    public String description;
    public String creationdate;
    public int status;
    public boolean isSynced = true;
    public boolean isSyncFail = false;
    public ArrayList<String> imgUrls;

    public Complaint() {
    }

    protected Complaint(Parcel in) {
        complaintid = in.readString();
        societyId = in.readString();
        title = in.readString();
        description = in.readString();
        creationdate = in.readString();
        status = in.readInt();
        isSynced = in.readByte() != 0;
        isSyncFail = in.readByte() != 0;
        imgUrls = in.createStringArrayList();
    }

    public static Comparator getDescendingIdComparator() {
        return new Comparator<Complaint>() {
            @Override
            public int compare(Complaint o1, Complaint o2) {
                return Integer.parseInt(o1.complaintid) < Integer.parseInt(o2.complaintid) ? 1 : -1;
            }
        };
    }

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
        dest.writeInt(status);
        dest.writeByte((byte) (isSynced ? 1 : 0));
        dest.writeByte((byte) (isSyncFail ? 1 : 0));
        dest.writeStringList(imgUrls);
    }

    public void setFromObject(Complaint complaint) {
        this.complaintid = complaint.complaintid;
        this.societyId = complaint.societyId;
        this.title = complaint.title;
        this.description = complaint.description;
        this.creationdate = complaint.creationdate;
        this.status = complaint.status;
        this.isSynced = complaint.isSynced;
        this.isSyncFail = complaint.isSyncFail;
        this.imgUrls = complaint.imgUrls;
    }

    public void addImageUrl(String imgUrl) {
        if (imgUrls == null)
            imgUrls = new ArrayList<String>();
        imgUrls.add(imgUrl);
    }


    public interface Status {
        int NEW = 1;
        int COMPLETED = 2;
        int IN_PROGRESS = 3;
        int REJECTED = 4;
    }
}
