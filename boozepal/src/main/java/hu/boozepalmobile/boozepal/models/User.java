package hu.boozepalmobile.boozepal.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fanny on 2016.10.09..
 */

public class User implements Parcelable{

    private Long id;
    private String name;
    private String city;
    private List<String> boozes;
    private List<String> pubs;
    private List<Date> savedDates;
    private int searchRadius;
    private int priceCategory;
    private List<User> myPals;

    public User(Long id, String name, String city, List<String> boozes, List<String> pubs, int searchRadius, int priceCategory, List<Date> savedDates, List<User> myPals) {
        this.id = id;
        this.name = name;
        this.city = city;
        //this.gender = gender;
        this.boozes = boozes;
        this.pubs = pubs;
        this.searchRadius = searchRadius;
        this.priceCategory = priceCategory;
        this.savedDates = savedDates;
        this.myPals = myPals;
    }

    // Parcelling part
    public User(Parcel in){
        this.id = in.readLong();
        this.name = in.readString();
        this.city = in.readString();
        //this.gender = in.readString();
        this.boozes = new ArrayList<String>();
        in.readList(boozes,null);
        this.pubs = new ArrayList<String>();
        in.readList(pubs,null);
        this.searchRadius = in.readInt();
        this.priceCategory = in.readInt();
        this.savedDates = new ArrayList<Date>();
        in.readList(savedDates, null);
        this.myPals = new ArrayList<>();
        in.readList(myPals, null);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.city);
        //dest.writeString(this.gender);
        dest.writeList(this.boozes);
        dest.writeList(this.pubs);
        dest.writeInt(this.searchRadius);
        dest.writeInt(this.priceCategory);
        dest.writeList(this.savedDates);
        dest.writeList(this.myPals);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public Long getId() {
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
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

    public List<Date> getSavedDates() {
        return savedDates;
    }

    public void setSavedDates(List<Date> savedDates) {
        this.savedDates = savedDates;
    }

    public List<User> getMyPals() {
        return myPals;
    }

    public void setMyPals(List<User> myPals) {
        this.myPals = myPals;
    }

    public void addBooze(String booze){
        this.boozes.add(booze);
    }

    public void addPub(String pub){
        this.pubs.add(pub);
    }

    public void addNewPal(User pal){
        this.myPals.add(pal);
    }

    public void addDate(Date date){
        this.savedDates.add(date);
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, User.class);
    }
}
