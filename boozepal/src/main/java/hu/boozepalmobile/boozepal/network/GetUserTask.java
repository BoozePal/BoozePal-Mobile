package hu.boozepalmobile.boozepal.network;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.activities.CalendarActivity;
import hu.boozepalmobile.boozepal.models.User;

/**
 * Created by fanny on 2016.11.26..
 */

public class GetUserTask extends AsyncTask<String, Void, User> {

    private final String TAG = "GetUserTask";
    private Context context;

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
        //System.out.println(s);

        //super.onPostExecute(s);
    }

    private User getUser(String token) {
        URL url = null;
        InputStream is = null;
        User result = null;
        try {
            url = new URL(context.getString(R.string.rest_url_getuser) + token);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
           // conn.setRequestProperty("Content-Type", "application/json");
            //conn.setRequestProperty("Accept", "*/*");
            conn.setDoInput(true);
            //conn.setDoOutput(true);
            conn.connect();

            JSONObject json = new JSONObject();
            json.put("token",token);

            System.out.println(json.toString());

            /*OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(json.toString());
            writer.close();
            os.close();*/

            //System.out.println(conn.getResponseMessage());

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();

            Log.d(TAG, sb.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;

    }
}