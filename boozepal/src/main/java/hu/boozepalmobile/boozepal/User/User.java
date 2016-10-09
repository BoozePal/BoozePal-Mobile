package hu.boozepalmobile.boozepal.User;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanny on 2016.10.09..
 */

public class User implements Parcelable{
    private String id;
    private String name;
    private String gender;
    private List<String> boozes;
    private List<String> pubs;

    public User(String id, String name, String gender){
        this.id = id;
        this.name = name;
        this.gender = gender;
    }

    public User(String id, String name, String gender, List<String> boozes, List<String> pubs) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.boozes = boozes;
        this.pubs = pubs;
    }

    // Parcelling part
    public User(Parcel in){
        this.id = in.readString();
        this.name = in.readString();
        this.gender = in.readString();
        this.boozes = new ArrayList<String>();
        in.readList(boozes,null);
        this.pubs = new ArrayList<String>();
        in.readList(pubs,null);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.gender);
        dest.writeList(this.boozes);
        dest.writeList(this.pubs);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public List<String> getBoozes() {
        return boozes;
    }

    public List<String> getPubs() {
        return pubs;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBoozes(List<String> boozes) {
        this.boozes = boozes;
    }

    public void setPubs(List<String> pubs) {
        this.pubs = pubs;
    }

}
