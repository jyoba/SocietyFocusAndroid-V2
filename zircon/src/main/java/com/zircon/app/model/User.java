package com.zircon.app.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by jikoobaruah on 21/01/16.
 */
public class User implements Parcelable {

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    public String userid;
    public String firstname;
    public String lastname;
    public String occupation;
    public String email;
    public String description;
    @SerializedName("contact_no")
    public String contactNumber;
    @SerializedName("profile_pic")
    public String profilePic;
    public ArrayList<UserRole> userRoles;
    public String address;

    protected User(Parcel in) {
        userid = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        occupation = in.readString();
        email = in.readString();
        description = in.readString();
        contactNumber = in.readString();
        profilePic = in.readString();
        address = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userid);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(occupation);
        dest.writeString(email);
        dest.writeString(description);
        dest.writeString(contactNumber);
        dest.writeString(profilePic);
        dest.writeString(address);
    }

    public String getFullName() {
        return (this.firstname + " " + (this.lastname != null ? this.lastname : "")).toUpperCase();
    }

    public String getOccupation() {
        return TextUtils.isEmpty(occupation) ? "Resident" : occupation;
    }

    public static class UserRole {
        public String id;
        public String userRole;

    }
}