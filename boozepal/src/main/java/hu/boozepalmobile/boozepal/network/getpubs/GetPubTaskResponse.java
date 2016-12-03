package hu.boozepalmobile.boozepal.network.getpubs;

import java.util.ArrayList;
import java.util.HashMap;

import hu.boozepalmobile.boozepal.models.Pub;

/**
 * Created by fanny on 2016.12.01..
 */

public interface GetPubTaskResponse {
    void onTaskFinished(ArrayList<Pub> pubs);
}
