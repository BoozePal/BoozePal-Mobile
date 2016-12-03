package hu.boozepalmobile.boozepal.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import hu.boozepalmobile.boozepal.models.PalRequest;
import hu.boozepalmobile.boozepal.models.Pub;
import hu.boozepalmobile.boozepal.models.User;

/**
 * Created by fanny on 2016.12.03..
 */

public class UIPalRequest implements Parcelable {
    private User requesterUser;
    private Pub pub;
    private Date date;
    private boolean accepted;

    public UIPalRequest(Parcel in) {
        requesterUser = in.readParcelable(User.class.getClassLoader());
        pub = in.readParcelable(Pub.class.getClassLoader());
        date = new Date(in.readLong());
        accepted = in.readByte() != 0;
    }

    public UIPalRequest(User requesterUser, Pub pub, Date date, boolean accepted) {
        this.requesterUser = requesterUser;
        this.pub = pub;
        this.date = date;
        this.accepted = accepted;
    }

    public static final Creator<UIPalRequest> CREATOR = new Creator<UIPalRequest>() {
        @Override
        public UIPalRequest createFromParcel(Parcel in) {
            return new UIPalRequest(in);
        }

        @Override
        public UIPalRequest[] newArray(int size) {
            return new UIPalRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.requesterUser,0);
        dest.writeParcelable(pub, 0);
        dest.writeLong(date.getTime());
        dest.writeByte((byte) (accepted ? 1 : 0));
    }

    public User getRequesterUser() {
        return requesterUser;
    }

    public void setRequesterUser(User requesterUser) {
        this.requesterUser = requesterUser;
    }

    public Pub getPub() {
        return pub;
    }

    public void setPub(Pub pub) {
        this.pub = pub;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

   public User getUser() {
        return requesterUser;
    }

    public void setUser(User user) {
        this.requesterUser = user;
    }

    @Override
    public String toString() {
        return "UIPalRequest{" +
                "accepted=" + accepted +
                ", date=" + date +
                ", pub=" + pub +
                ", requesterUser=" + requesterUser +
                '}';
    }
}
