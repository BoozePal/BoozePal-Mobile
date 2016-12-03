package hu.boozepalmobile.boozepal.network.findpals;

import java.util.ArrayList;

import hu.boozepalmobile.boozepal.models.User;

/**
 * Created by fanny on 2016.11.26..
 */

public interface FindPalsResponse {
    void onTaskFinished(ArrayList<User> result);
}
