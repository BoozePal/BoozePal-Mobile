package hu.boozepalmobile.boozepal.models;

/**
 * Created by fanny on 2016.11.26..
 */

public class Token {
    private static String token;

    public Token(String token) {
        this.token = token;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String t) {
        token = t;
    }
}
