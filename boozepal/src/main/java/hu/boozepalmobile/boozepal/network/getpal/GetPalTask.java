package hu.boozepalmobile.boozepal.network.getpal;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.models.User;

/**
 * Created by fanny on 2016.12.03..
 */

public class GetPalTask extends AsyncTask<Long, Void, User> {

    private final String TAG = "GetPalTask";

    public GetPalResponse delegate = null;

    private Context context;

    public GetPalTask(Context context){
        this.context = context;
    }

    @Override
    protected User doInBackground(Long... params) {
        User user = getPal(params[0]);
        return user;
    }

    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);
        if(this.delegate != null)
            this.delegate.onTaskFinished(user, "GETPAL");
    }

    private User getPal(Long id){
        URL url = null;
        InputStream is = null;
        User result = null;
        try {
            url = new URL(this.context.getString(R.string.rest_url_getpal) + id);

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
            }).create().fromJson(sb.toString(), User.class);

            Log.d(TAG, result.toString());

            return result;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
