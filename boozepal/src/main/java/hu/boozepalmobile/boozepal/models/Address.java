package hu.boozepalmobile.boozepal.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fanny on 2016.11.30..
 */

public class Address implements Parcelable {

    private String town;

    private String street;

    public Address(String town, String street) {
        this.town = town;
        this.street = street;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }


    public Address(Parcel in){
        this.town = in.readString();
        this.street = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.town);
        dest.writeString(this.street);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
