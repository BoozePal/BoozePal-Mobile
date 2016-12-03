package hu.boozepalmobile.boozepal.network.getuser;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.models.User;
import hu.boozepalmobile.boozepal.network.getuser.GetUserResponse;

/**
 * Created by fanny on 2016.11.26..
 */

public class GetUserTask extends AsyncTask<String, Void, User> {

    private final String TAG = "GetUserTask";
    private Context context;

    public GetUserResponse delegate = null;

    public GetUserTask(Context context) {
        this.context = context;
    }

    @Override
    protected User doInBackground(String... params) {
        User result = getUser(params[0]);
        return result;
    }

    @Override
    protected void onPostExecute(User u) {
        super.onPostExecute(u);
        delegate.onTaskFinished(u, "GETUSER");
    }

    private User getUser(String token) {
        User result = null;
        try {
            URL url = new URL(context.getString(R.string.rest_url_getuser) + token);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();

            Log.d(TAG, sb.toString());

            result = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            }).create().fromJson(sb.toString(),User.class);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }
}