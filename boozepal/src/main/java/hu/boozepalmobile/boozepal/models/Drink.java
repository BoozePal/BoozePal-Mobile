package hu.boozepalmobile.boozepal.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fanny on 2016.11.28..
 */

public class Drink implements Parcelable {

    private int id;

    private String name;

    private DrinkTypeEnum drinkType;

    public Drink(int id, String name, DrinkTypeEnum drinkType) {
        this.id = id;
        this.name = name;
        this.drinkType = drinkType;
    }

    public Drink() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DrinkTypeEnum getDrinkType() {
        return drinkType;
    }

    public void setDrinkType(DrinkTypeEnum drinkType) {
        this.drinkType = drinkType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Drink(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.drinkType = DrinkTypeEnum.valueFor(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.getId());
        dest.writeString(this.getName());
        dest.writeString(this.getDrinkType().getValue());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Drink createFromParcel(Parcel in) {
            return new Drink(in);
        }

        public Drink[] newArray(int size) {
            return new Drink[size];
        }
    };

    @Override
    public String toString() {
        return drinkType + " - " + name;
    }
}
