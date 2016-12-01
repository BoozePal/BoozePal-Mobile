package hu.boozepalmobile.boozepal.network;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

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

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.models.User;

/**
 * Created by fanny on 2016.11.28..
 */

public class UpdateUserTask extends AsyncTask<User,Void,Integer>{

    private Context context;
    private String token;

    public UpdateUserTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
    }

    @Override
    protected Integer doInBackground(User... params) {
        return 0;
    }

    private String saveSettings(User user) {
        return null;
    }
}
