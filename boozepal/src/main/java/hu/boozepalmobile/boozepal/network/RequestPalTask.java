package hu.boozepalmobile.boozepal.network;

import android.content.Context;
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

import hu.boozepalmobile.boozepal.models.Token;
import hu.boozepalmobile.boozepal.models.User;

/**
 * Created by fanny on 2016.11.27..
 */

public class RequestPalTask extends AsyncTask<User, Void, Integer>{

    public FindPalsResponse delegate = null;
    private Context context;

    public RequestPalTask(Context context){
        this.context = context;
    }

    @Override
    protected Integer doInBackground(User... params) {
        int result = request(params[0], params[1]);
        return result;
    }

    @Override
    protected void onPostExecute(Integer i) {
        //System.out.println(s);

        //super.onPostExecute(s);
    }

    private int request(User loggedUser, User requestedUser) {
        URL url = null;
        InputStream is = null;
        int result = 0;
        try {
            //url = new URL();

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
            obj.put("token", Token.getToken());
            //obj.put("use")

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
