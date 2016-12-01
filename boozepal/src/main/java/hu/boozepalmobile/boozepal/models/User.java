package hu.boozepalmobile.boozepal.models;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fanny on 2016.10.09..
 */

public class User implements Parcelable {

    private Long id;

    private String name;

    private String city;

    private List<Drink> boozes;

    private List<Pub> pubs;

    private List<Date> savedDates;

    private int searchRadius;

    private int priceCategory;

    //private List<User> myPals;

    private Coordinate lastKnownCoordinate;

    private Map<User, PalRequest> actualPals;

    public User() {
    }

    public User(Long id, String name, String city, List<Drink> boozes, List<Pub> pubs, List<Date> savedDates, int searchRadius, int priceCategory, Coordinate lastKnownCoordinate, Map<User, PalRequest> actualPals) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.boozes = boozes;
        this.pubs = pubs;
        this.savedDates = savedDates;
        this.searchRadius = searchRadius;
        this.priceCategory = priceCategory;
        //this.myPals = myPals;
        this.lastKnownCoordinate = lastKnownCoordinate;
        this.actualPals = actualPals;
    }

    public User(Long id, String name, String city, List<Drink> boozes, List<Pub> pubs, int searchRadius, int priceCategory, List<Date> savedDates, List<User> myPals) {
        this.id = id;
        this.name = name;
        this.city = city;
        //this.gender = gender;
        this.boozes = boozes;
        this.pubs = pubs;
        this.searchRadius = searchRadius;
        this.priceCategory = priceCategory;
        this.savedDates = savedDates;
        //this.myPals = myPals;
        this.lastKnownCoordinate = null;
    }

    // Parcelling part
    public User(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.city = in.readString();
        this.boozes = in.readArrayList(Drink.class.getClassLoader());
        //in.readList(boozes, null);
        this.pubs = in.readArrayList(Pub.class.getClassLoader());
        //in.readList(pubs, null);
        this.searchRadius = in.readInt();
        this.priceCategory = in.readInt();
        this.savedDates = new ArrayList<Date>();
        in.readList(savedDates, null);
        //this.myPals = new ArrayList<>();
        //in.readList(myPals, null);
        this.lastKnownCoordinate = in.readParcelable(Coordinate.class.getClassLoader());
        this.actualPals = new HashMap<>();
        int size = in.readInt();
        for(int i = 0; i < size; i++){
            User key = in.readParcelable(User.class.getClassLoader());
            PalRequest value = in.readParcelable(PalRequest.class.getClassLoader());
            this.actualPals.put(key,value);
        }

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
        //dest.writeList(this.myPals);
        dest.writeParcelable(this.lastKnownCoordinate, 0);
        dest.writeInt(actualPals.size());
        for(Map.Entry<User,PalRequest> entry : actualPals.entrySet()){
            dest.writeParcelable(entry.getKey(),0);
            dest.writeParcelable(entry.getValue(),0);
        }
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

    public List<Drink> getBoozes() {
        return boozes;
    }

    public List<Pub> getPubs() {
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

    public void setBoozes(List<Drink> boozes) {
        this.boozes = boozes;
    }

    public void setPubs(List<Pub> pubs) {
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

    public void addBooze(Drink booze) {
        this.boozes.add(booze);
    }

    public void addPub(Pub pub) {
        this.pubs.add(pub);
    }

    public void addDate(Date date) {
        this.savedDates.add(date);
    }

    public Coordinate getCurrentLocation() {
        return lastKnownCoordinate;
    }

    public Map<User, PalRequest> getActualPals() {
        return actualPals;
    }

    public void setActualPals(Map<User, PalRequest> actualPals) {
        this.actualPals = actualPals;
    }

    public void setCurrentLocation(Coordinate currentLocation) {
        this.lastKnownCoordinate = currentLocation;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, User.class);
    }
}
