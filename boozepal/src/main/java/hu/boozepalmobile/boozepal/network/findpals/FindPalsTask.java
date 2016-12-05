package hu.boozepalmobile.boozepal.network.findpals;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

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
import java.util.List;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.models.User;
import hu.boozepalmobile.boozepal.network.findpals.FindPalsResponse;

/**
 * Created by fanny on 2016.11.26..
 */

public class FindPalsTask extends AsyncTask<User, Void, ArrayList<User>> {

    private final String TAG = "FindPalsTask";

    public FindPalsResponse delegate = null;

    private String token;
    private Context context;

    public FindPalsTask(Context context, String token) {
        this.context = context;
        this.token = token;
    }

    @Override
    protected ArrayList<User> doInBackground(User... params) {
        Log.d(TAG, "FindPalsTask started!");
        ArrayList<User> users = findPals(params[0]);
        return users;
    }

    @Override
    protected void onPostExecute(ArrayList<User> users) {
        super.onPostExecute(users);
        delegate.onTaskFinished(users);
    }

    private ArrayList<User> findPals(User user) {
        URL url = null;
        InputStream is = null;
        ArrayList<User> users = new ArrayList<>();
        try {
            url = new URL(context.getString(R.string.rest_url_findpals));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "*/*");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            String js = new GsonBuilder().create().toJson(user, User.class);

            System.out.println(user.toString());
            System.out.println(js.toString());

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(js.toString());
            writer.close();
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();

            Log.d(TAG, sb.toString());

            Type listType = new TypeToken<List<User>>() {
            }.getType();

            users = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            }).create().fromJson(sb.toString(), listType);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "FindPalsTask ended!");
        return users;

    }
}
