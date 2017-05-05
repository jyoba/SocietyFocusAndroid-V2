package com.zircon.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jikoobaruah on 08/04/17.
 */

public class NoticeBoard implements Parcelable {

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


    protected NoticeBoard(Parcel in) {
        id = in.readInt();
        imageUrl2 = in.readString();
        title = in.readString();
        imageUrl1 = in.readString();
        status = in.readInt();
        modifieDate = in.readString();
        description = in.readString();
        isPinned = in.readString();
        score = in.readInt();
        creationDate = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<NoticeBoard> CREATOR = new Creator<NoticeBoard>() {
        @Override
        public NoticeBoard createFromParcel(Parcel in) {
            return new NoticeBoard(in);
        }

        @Override
        public NoticeBoard[] newArray(int size) {
            return new NoticeBoard[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(imageUrl2);
        dest.writeString(title);
        dest.writeString(imageUrl1);
        dest.writeInt(status);
        dest.writeString(modifieDate);
        dest.writeString(description);
        dest.writeString(isPinned);
        dest.writeInt(score);
        dest.writeString(creationDate);
        dest.writeParcelable(user, flags);
    }
}