package hu.boozepalmobile.boozepal.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fanny on 2016.11.30..
 */

public class Pub implements Parcelable {

    /**
     * ID of the pub.
     */
    private Long id;

    /**
     * Name of the pub.
     */
    private String name;

    /**
     * Address of the pub.
     */
    private Address address;

    /**
     * The open hours.
     */
    private String openHours;

    /**
     * Price category of the pub.
     */
    private Integer priceCategory;

    public Pub(Long id, String name, Address address, String openHours, Integer priceCategory) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.openHours = openHours;
        this.priceCategory = priceCategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        this.id = in.readLong();
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
        dest.writeLong(this.id);
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

    @Override
    public String toString() {
        return this.name + " " + this.address.getTown();
    }
}

