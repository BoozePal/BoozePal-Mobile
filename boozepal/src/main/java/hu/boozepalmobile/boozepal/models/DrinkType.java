package hu.boozepalmobile.boozepal.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanny on 2016.11.29..
 */

public class DrinkType {
    private int id;
    private DrinkTypeEnum drinkType;
    private List<Drink> drinks;

    public DrinkType() {
        this.drinks = new ArrayList<>();
    }

    public DrinkType(int id, DrinkTypeEnum drinkType, List<Drink> drinks) {
        this.id = id;
        this.drinkType = drinkType;
        this.drinks = drinks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DrinkTypeEnum getDrinkType() {
        return drinkType;
    }

    public void setDrinkType(DrinkTypeEnum drinkType) {
        this.drinkType = drinkType;
    }

    public List<Drink> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<Drink> drinks) {
        this.drinks = drinks;
    }
}
