package hu.boozepalmobile.boozepal.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.models.Token;
import hu.boozepalmobile.boozepal.models.User;
import hu.boozepalmobile.boozepal.utils.RequestJSON;
import hu.boozepalmobile.boozepal.utils.UpdateUserJSON;

/**
 * Created by fanny on 2016.11.27..
 */

public class RequestPalTask extends AsyncTask<User, Void, User> {

    private final String TAG = "RequestPalTask";

    public RequestPalResponse delegate = null;
    private Context context;
    private Date date;
    private String token;

    public RequestPalTask(Context context, Date date) {
        this.context = context;
        this.date = date;
    }

    @Override
    protected User doInBackground(User... params) {
        User result = request(params[0], params[1]);
        return result;
    }

    @Override
    protected void onPostExecute(User u) {
        super.onPostExecute(u);
        delegate.onTaskFinished(u);
    }

    private User request(User loggedUser, User requestedUser) {
        URL url = null;
        InputStream is = null;
        User result = null;
        try {
            url = new URL(context.getString(R.string.rest_url_sendRequest));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "*/*");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            RequestJSON reqjson = new RequestJSON(loggedUser.getId(), requestedUser.getId(), this.date, null);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(reqjson.toString());
            writer.close();
            os.close();

            System.out.println(conn.getResponseMessage());

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "Request OK");
                result = loggedUser;
            } else {
                Log.d(TAG, "Error occured during request");
                result = loggedUser;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
