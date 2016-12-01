package hu.boozepalmobile.boozepal.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fanny on 2016.11.28..
 */

public enum DrinkTypeEnum {
    /**
     *
     */
    BEER("beer"),
    /**
     *
     */
    WINE("wine"),
    /**
     *
     */
    WHISKEY("whiskey"),
    /**
     *
     */
    GIN("gin"),
    /**
     *
     */
    VODKA("vodka"),
    /**
     *
     */
    BRANDY("brandy"),
    /**
     *
     */
    CHAMPAGNE("champagne"),
    /**
     *
     */
    RUM("rum"),
    /**
     *
     */
    UNKNOWN("unknown");

    private String value;

    private DrinkTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private static final Map<String, DrinkTypeEnum> map = new HashMap<>();

    static {
        for (DrinkTypeEnum en : values()) {
            map.put(en.value, en);
        }
    }

    public static DrinkTypeEnum valueFor(String name) {
        return map.get(name);
    }
}
