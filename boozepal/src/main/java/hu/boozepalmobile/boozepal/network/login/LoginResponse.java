package hu.boozepalmobile.boozepal.network.login;

import hu.boozepalmobile.boozepal.models.User;

/**
 * Created by fanny on 2016.11.30..
 */

public interface LoginResponse {
    void onTaskFinished(User result);
}
