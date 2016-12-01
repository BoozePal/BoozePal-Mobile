package hu.boozepalmobile.boozepal.utils;

import com.google.gson.GsonBuilder;

import hu.boozepalmobile.boozepal.models.User;

/**
 * Created by fanny on 2016.11.28..
 */

public class FindPalsJSON {
    private String token;
    private User user;

    public FindPalsJSON(String token, User user) {
        this.token = token;
        this.user = user;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, FindPalsJSON.class);
    }
}
