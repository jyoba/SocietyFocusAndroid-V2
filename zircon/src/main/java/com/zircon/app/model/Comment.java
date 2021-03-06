package com.zircon.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jikoobaruah on 05/05/17.
 */

public class Comment implements Parcelable {

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
    public String commentid;
    public User user;
    public String comment;
    public String complaintid;
    public String creationdate;
    public int status = Status.FROM_SERVER;

    public Comment() {
    }
    protected Comment(Parcel in) {
        commentid = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        comment = in.readString();
        complaintid = in.readString();
        creationdate = in.readString();
        status = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(commentid);
        dest.writeParcelable(user, flags);
        dest.writeString(comment);
        dest.writeString(complaintid);
        dest.writeString(creationdate);
        dest.writeInt(status);
    }

    public void setFromObject(Comment comment) {

        this.commentid = comment.commentid;
        this.user = comment.user;
        this.comment = comment.comment;
        this.complaintid = comment.complaintid;
        this.creationdate = comment.creationdate;
        this.status = comment.status;

    }
    public interface Status {
        int FROM_SERVER = 0;
        int SENDING_TO_SERVER = 1;
        int SENDING_TO_SERVER_FAIL = 2;
    }
}
