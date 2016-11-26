package hu.boozepalmobile.boozepal.network;

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
import java.net.ProtocolException;
import java.net.URL;

import hu.boozepalmobile.boozepal.activities.CalendarActivity;

/**
 * Created by fanny on 2016.11.26..
 */

public class LogoutTask extends AsyncTask<String, Void, Integer> {

    private String token;
    private String url;

    @Override
    protected Integer doInBackground(String... params) {
        token = params[0];
        url = params[1];
        int result = save();
        return result;
    }

    @Override
    protected void onPostExecute(Integer i) {
        //System.out.println(s);

        //super.onPostExecute(s);
    }

    private int save() {
        URL url = null;
        InputStream is = null;
        int result = 0;
        try {
            url = new URL(this.url);

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


            System.out.println(obj.toString());

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(obj.toString());
            writer.close();
            os.close();

            System.out.println(conn.getResponseMessage());

            result = conn.getResponseCode();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                Log.d("LogoutTask", "Remote logout OK");
            }
            else{
                Log.d("LogoutTask", "Error occured during remote logout");
            }

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