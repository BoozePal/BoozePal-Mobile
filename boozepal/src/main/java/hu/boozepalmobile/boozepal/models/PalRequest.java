package hu.boozepalmobile.boozepal.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by fanny on 2016.11.30..
 */

public class PalRequest implements Parcelable{
    private Long requesterUserId;
    private Pub pub;
    private Date date;
    private boolean accepted;

    protected PalRequest(Parcel in) {
        requesterUserId = in.readLong();
        pub = in.readParcelable(Pub.class.getClassLoader());
        date = new Date(in.readLong());
        accepted = in.readByte() != 0;
    }

    public static final Creator<PalRequest> CREATOR = new Creator<PalRequest>() {
        @Override
        public PalRequest createFromParcel(Parcel in) {
            return new PalRequest(in);
        }

        @Override
        public PalRequest[] newArray(int size) {
            return new PalRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(requesterUserId);
        dest.writeParcelable(pub, 0);
        dest.writeLong(date.getTime());
        dest.writeByte((byte) (accepted ? 1 : 0));
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

    public Long getRequesterUserId() {
        return requesterUserId;
    }

    public void setRequesterUserId(Long requesterUserId) {
        this.requesterUserId = requesterUserId;
    }

    @Override
    public String toString() {
        return "PalRequest{" +
                "pub=" + pub +
                ", date=" + date +
                ", accepted=" + accepted +
                '}';
    }
}
