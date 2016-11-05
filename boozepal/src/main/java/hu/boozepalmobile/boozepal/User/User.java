package hu.boozepalmobile.boozepal.User;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanny on 2016.10.09..
 */

public class User implements Parcelable{
    private String id;
    private String name;
    private String city;
    //private String gender;
    private List<String> boozes;
    private List<String> pubs;
    private int searchRadius;
    private int priceCategory;

    private String token;

    public User(String id, String name, String city, List<String> boozes, List<String> pubs, int searchRadius, int priceCategory, String token) {
        this.id = id;
        this.name = name;
        this.city = city;
        //this.gender = gender;
        this.boozes = boozes;
        this.pubs = pubs;
        this.searchRadius = searchRadius;
        this.priceCategory = priceCategory;
        this.token = token;
    }

    // Parcelling part
    public User(Parcel in){
        this.id = in.readString();
        this.name = in.readString();
        this.city = in.readString();
        //this.gender = in.readString();
        this.boozes = new ArrayList<String>();
        in.readList(boozes,null);
        this.pubs = new ArrayList<String>();
        in.readList(pubs,null);
        this.searchRadius = in.readInt();
        this.priceCategory = in.readInt();
        this.token = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.city);
        //dest.writeString(this.gender);
        dest.writeList(this.boozes);
        dest.writeList(this.pubs);
        dest.writeInt(this.searchRadius);
        dest.writeInt(this.priceCategory);
        dest.writeString(this.token);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    /*public String getGender() {
        return gender;
    }*/

    public List<String> getBoozes() {
        return boozes;
    }

    public List<String> getPubs() {
        return pubs;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    /*public void setGender(String gender) {
        this.gender = gender;
    }*/

    public void setBoozes(List<String> boozes) {
        this.boozes = boozes;
    }

    public void setPubs(List<String> pubs) {
        this.pubs = pubs;
    }

    public int getSearchRadius() {
        return searchRadius;
    }

    public void setSearchRadius(int searchRadius) {
        this.searchRadius = searchRadius;
    }

    public int getPriceCategory() {
        return priceCategory;
    }

    public void setPriceCategory(int priceCategory) {
        this.priceCategory = priceCategory;
    }

}
