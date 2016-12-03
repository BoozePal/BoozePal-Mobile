package hu.boozepalmobile.boozepal.network.getpal;

import java.util.ArrayList;

import hu.boozepalmobile.boozepal.models.User;

/**
 * Created by fanny on 2016.12.03..
 */

public interface GetPalResponse {
    void onTaskFinished(User result, String tag);
}
