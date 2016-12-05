package hu.boozepalmobile.boozepal.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fanny on 2016.11.30..
 */

public class User implements Parcelable {

    private Long id;

    private String username;

    private String password;

    private String email;

    private boolean remove;

    private List<Drink> favouriteDrinks;

    private List<Pub> favouritePub;

    private int priceCategory;

    private Address address;

    private Coordinate lastKnownCoordinate;

    private int searchRadius;

    private Map<Long, PalRequest> actualPals;

    private boolean loggedIn;

    private List<Date> timeBoard;

    public User(Long id, String username, String password, String email, boolean remove, List<Drink> favouriteDrinks, List<Pub> favouritePub, int priceCategory, Address address, Coordinate lastKnownCoordinate, int searchRadius, Map<Long, PalRequest> actualPals, boolean loggedIn, List<Date> timeBoard) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.remove = remove;
        this.favouriteDrinks = favouriteDrinks;
        this.favouritePub = favouritePub;
        this.priceCategory = priceCategory;
        this.address = address;
        this.lastKnownCoordinate = lastKnownCoordinate;
        this.searchRadius = searchRadius;
        this.actualPals = actualPals;
        this.loggedIn = loggedIn;
        this.timeBoard = timeBoard;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public List<Drink> getFavouriteDrinks() {
        return favouriteDrinks;
    }

    public void setFavouriteDrinks(List<Drink> favouriteDrinks) {
        this.favouriteDrinks = favouriteDrinks;
    }

    public List<Pub> getFavouritePub() {
        return favouritePub;
    }

    public void setFavouritePub(List<Pub> favouritePub) {
        this.favouritePub = favouritePub;
    }

    public int getPriceCategory() {
        return priceCategory;
    }

    public void setPriceCategory(int priceCategory) {
        this.priceCategory = priceCategory;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Coordinate getLastKnownCoordinate() {
        return lastKnownCoordinate;
    }

    public void setLastKnownCoordinate(Coordinate lastKnownCoordinate) {
        this.lastKnownCoordinate = lastKnownCoordinate;
    }

    public int getSearchRadius() {
        return searchRadius;
    }

    public void setSearchRadius(int searchRadius) {
        this.searchRadius = searchRadius;
    }

    public Map<Long, PalRequest> getActualPals() {
        return actualPals;
    }

    public void setActualPals(Map<Long, PalRequest> actualPals) {
        this.actualPals = actualPals;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public List<Date> getTimeBoard() {
        return timeBoard;
    }

    public void setTimeBoard(List<Date> timeBoard) {
        this.timeBoard = timeBoard;
    }

    public User() {
        this.favouriteDrinks = new ArrayList<>();
        this.favouritePub = new ArrayList<>();
        this.timeBoard = new ArrayList<>();
        this.actualPals = new HashMap<>();
    }

    public User(Parcel in) {
        this.id = in.readLong();
        this.username = in.readString();
        this.password = in.readString();
        this.email = in.readString();
        this.remove = in.readByte() != 0;
        this.favouriteDrinks = new ArrayList<>();
        this.favouriteDrinks = in.readArrayList(Drink.class.getClassLoader());
        this.favouritePub = new ArrayList<>();
        this.favouritePub = in.readArrayList(Pub.class.getClassLoader());
        this.priceCategory = in.readInt();
        this.address = in.readParcelable(Address.class.getClassLoader());
        this.lastKnownCoordinate = in.readParcelable(Coordinate.class.getClassLoader());
        this.searchRadius = in.readInt();
        int size = in.readInt();
        this.actualPals = new HashMap<>();
        for (int i = 0; i < size; i++) {
            Long key = in.readLong();
            PalRequest value = in.readParcelable(PalRequest.class.getClassLoader());
            this.actualPals.put(key, value);
        }
        this.loggedIn = in.readByte() != 0;
        this.timeBoard = new ArrayList<>();
        this.timeBoard = in.readArrayList(Date.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.email);
        dest.writeByte((byte) (this.remove ? 1 : 0));
        dest.writeList(this.favouriteDrinks);
        dest.writeList(this.favouritePub);
        dest.writeInt(this.priceCategory);
        dest.writeParcelable(this.address, 0);
        dest.writeParcelable(this.lastKnownCoordinate, 0);
        dest.writeInt(this.searchRadius);
        dest.writeInt(actualPals.size());
        for (Map.Entry<Long, PalRequest> entry : actualPals.entrySet()) {
            dest.writeLong(entry.getKey());
            dest.writeParcelable(entry.getValue(), 0);
        }
        dest.writeByte((byte) (loggedIn ? 1 : 0));
        dest.writeList(this.timeBoard);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public void addBooze(Drink booze) {
        this.favouriteDrinks.add(booze);
    }

    public void addPub(Pub pub) {
        this.favouritePub.add(pub);
    }

    public void addDate(Date date) {
        this.timeBoard.add(date);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", remove=" + remove +
                ", favouriteDrinks=" + favouriteDrinks +
                ", favouritePub=" + favouritePub +
                ", priceCategory=" + priceCategory +
                ", address=" + address +
                ", lastKnownCoordinate=" + lastKnownCoordinate +
                ", searchRadius=" + searchRadius +
                ", actualPals=" + actualPals +
                ", loggedIn=" + loggedIn +
                ", timeBoard=" + timeBoard +
                '}';
    }
}
