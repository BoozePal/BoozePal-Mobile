package hu.boozepalmobile.boozepal.models;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by fanny on 2016.11.30..
 */

public class RemoteUser {

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

    private Map<User, PalRequest> actualPals;

    private boolean loggedIn;

    private List<Date> timeBoard;

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

    public Map<User, PalRequest> getActualPals() {
        return actualPals;
    }

    public void setActualPals(Map<User, PalRequest> actualPals) {
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
}
