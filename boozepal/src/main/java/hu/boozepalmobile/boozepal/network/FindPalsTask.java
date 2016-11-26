package hu.boozepalmobile.boozepal.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
import java.net.URL;
import java.util.ArrayList;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.models.User;

/**
 * Created by fanny on 2016.11.26..
 */

public class FindPalsTask extends AsyncTask<User, Void, ArrayList<User>> {

    private final String TAG = "FindPalsTask";

    public FindPalsResponse delegate = null;

    private String token;
    private Context context;

    public FindPalsTask(Context context, String token){
        this.context = context;
        this.token = token;
    }

    @Override
    protected ArrayList<User> doInBackground(User... params) {
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

            JSONObject obj = new JSONObject();
            obj.put("token", this.token);
            obj.put("user", user);

            System.out.println(obj.toString());

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(obj.toString());
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
            String result = sb.toString();

            JSONObject jsonResult = new JSONObject(result);

            if(obj.length() == 0){
                Log.v(TAG, "Error occured during findPals!");
                return new ArrayList<>();
            }

            //TODO

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<User>();

    }
}
