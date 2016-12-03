package hu.boozepalmobile.boozepal.utils;

import com.google.gson.GsonBuilder;

/**
 * Created by fanny on 2016.12.03..
 */

public class RespondRequestJSON {
    /**
     * Felhasználó ID-ja.
    */
    private Long userId;

    /**
     * A megjelöltnek jelölt ID;
     */
    private Long requestedUserId;

    /**
     * Elfogadott állapota.
     */
    private boolean accepted;

    public RespondRequestJSON(Long userId, Long requestedUserId, boolean accepted) {
        this.userId = userId;
        this.requestedUserId = requestedUserId;
        this.accepted = accepted;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, RespondRequestJSON.class);
    }
}
