package hu.boozepalmobile.boozepal.network.requestpal;

import hu.boozepalmobile.boozepal.models.User;

/**
 * Created by fanny on 2016.11.27..
 */

public interface RequestPalResponse {
    void onTaskFinished(User result);
}
