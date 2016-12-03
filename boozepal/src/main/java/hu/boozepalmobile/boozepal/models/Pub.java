package hu.boozepalmobile.boozepal.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fanny on 2016.11.30..
 */

public class Pub implements Parcelable {

    private Long id;

    private String name;

    private Address address;

    private String openHours;

    private Integer priceCategory;

    /*private Long id;

    private String name;

    private String town;*/

    /*public Pub(Long id, String name, String town) {
        this.id = id;
        this.name = name;
        this.town = town;
    }*/

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

    /*public Long getId() {
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

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }*/

    public Pub(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        //this.town = in.readString();
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
        //dest.writeString(this.town);
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

