package hu.boozepalmobile.boozepal.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by fanny on 2016.11.30..
 */

public class PalRequest implements Parcelable{
    private Pub pub;

    private Date date;

    protected PalRequest(Parcel in) {
        pub = in.readParcelable(Pub.class.getClassLoader());
        date = new Date(in.readLong());
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
        dest.writeParcelable(pub, 0);
        dest.writeLong(date.getTime());
    }
}
