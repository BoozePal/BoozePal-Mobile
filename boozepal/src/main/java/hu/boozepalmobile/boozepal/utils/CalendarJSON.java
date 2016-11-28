package hu.boozepalmobile.boozepal.utils;

import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.List;

/**
 * Created by fanny on 2016.11.28..
 */

public class CalendarJSON {
    private String token;
    private List<String> timeTableList;

    public CalendarJSON(String token, List<String> timeTableList) {
        this.token = token;
        this.timeTableList = timeTableList;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, CalendarJSON.class);
    }
}
