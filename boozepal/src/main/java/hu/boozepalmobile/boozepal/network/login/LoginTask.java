package hu.boozepalmobile.boozepal.network.login;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.models.Drink;
import hu.boozepalmobile.boozepal.models.PalRequest;
import hu.boozepalmobile.boozepal.models.Pub;
import hu.boozepalmobile.boozepal.models.User;

/**
 * Created by fanny on 2016.11.26..
 */

public class LoginTask extends AsyncTask<String, Void, User> {

    private final String TAG = "LoginTask";

    public LoginResponse delegate = null;

    private Context context;

    public LoginTask(Context context) {
        this.context = context;
    }

    @Override
    protected User doInBackground(String... params) {
        User user = authenticate(params[0]);
        return user;
    }

    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);
        delegate.onTaskFinished(user);
    }

    private User authenticate(String token) {
        URL url = null;
        InputStream is = null;
        User result = null;
        try {
            url = new URL(context.getString(R.string.rest_url_login));

            System.out.println("wat");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "*/*");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            JSONObject sendObj = new JSONObject();
            sendObj.put("token", token);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(sendObj.toString());
            writer.close();
            os.close();

            //Read
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();

            Log.d(TAG,sb.toString());

            User user = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            }).enableComplexMapKeySerialization().create().fromJson(sb.toString(),User.class);

            if(user.getActualPals() == null)
                user.setActualPals(new HashMap<Long, PalRequest>());

            if(user.getTimeBoard() == null)
                user.setTimeBoard(new ArrayList<Date>());

            if(user.getFavouriteDrinks() == null)
                user.setFavouriteDrinks(new ArrayList<Drink>());

            if(user.getFavouritePub() == null)
                user.setFavouritePub(new ArrayList<Pub>());

            for(Long l: user.getActualPals().keySet()){
                System.out.println(user.getActualPals().get(l));
            }

            System.out.println(user.toString());
            result = user;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;

    }
}

