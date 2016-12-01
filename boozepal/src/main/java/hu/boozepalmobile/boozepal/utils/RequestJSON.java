package hu.boozepalmobile.boozepal.utils;

import com.google.gson.GsonBuilder;

import java.util.Date;

/**
 * Created by fanny on 2016.12.01..
 */

public class RequestJSON {
    private Long userId;
    private Long requestedUserId;
    private Date date;
    private Long pubId;

    public RequestJSON(Long userId, Long requestedUserId, Date date, Long pubId) {
        this.userId = userId;
        this.requestedUserId = requestedUserId;
        this.date = date;
        this.pubId = pubId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRequestedUserId() {
        return requestedUserId;
    }

    public void setRequestedUserId(Long requestedUserId) {
        this.requestedUserId = requestedUserId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getPubId() {
        return pubId;
    }

    public void setPubId(Long pubId) {
        this.pubId = pubId;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, RequestJSON.class);
    }
}
