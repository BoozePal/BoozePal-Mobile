package hu.boozepalmobile.boozepal.network.getuser;

import hu.boozepalmobile.boozepal.models.User;

/**
 * Created by fanny on 2016.12.01..
 */

public interface GetUserResponse {
    void onTaskFinished(User user, String tag);
}
