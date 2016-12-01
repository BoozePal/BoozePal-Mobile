package hu.boozepalmobile.boozepal.network;

import java.util.List;

import hu.boozepalmobile.boozepal.models.Pub;

/**
 * Created by fanny on 2016.12.01..
 */

public interface GetPubTaskResponse {
    void onTaskFinished(List<Pub> pubs);
}
