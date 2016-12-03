package hu.boozepalmobile.boozepal.network.getdrink;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.boozepalmobile.boozepal.models.Drink;
import hu.boozepalmobile.boozepal.models.DrinkType;
import hu.boozepalmobile.boozepal.models.DrinkTypeEnum;

/**
 * Created by fanny on 2016.11.29..
 */

public interface GetDrinkTaskResponse {
    void onTaskFinished(HashMap<DrinkTypeEnum, List<Drink>> result);
}
