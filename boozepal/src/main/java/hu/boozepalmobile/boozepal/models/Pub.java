package hu.boozepalmobile.boozepal.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fanny on 2016.11.30..
 */

public class Pub implements Parcelable {
    private String name;

    private Address address;

    private String openHours;

    private Integer priceCategory;

    public Pub(String name, Address address, String openHours, Integer priceCategory) {
        this.name = name;
        this.address = address;
        this.openHours = openHours;
        this.priceCategory = priceCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public Integer getPriceCategory() {
        return priceCategory;
    }

    public void setPriceCategory(Integer priceCategory) {
        this.priceCategory = priceCategory;
    }

    public Pub(Parcel in) {
        this.name = in.readString();
        this.address = in.readParcelable(Address.class.getClassLoader());
        this.openHours = in.readString();
        this.priceCategory = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeParcelable(this.address, 0);
        dest.writeString(this.openHours);
        dest.writeInt(this.priceCategory);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Pub createFromParcel(Parcel in) {
            return new Pub(in);
        }

        public Pub[] newArray(int size) {
            return new Pub[size];
        }
    };
}
