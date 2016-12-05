package hu.boozepalmobile.boozepal.network.respontrequest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.network.respontrequest.RespondRequestResponse;
import hu.boozepalmobile.boozepal.utils.RespondRequestJSON;

/**
 * Created by fanny on 2016.12.03..
 */

public class RespondRequestTask extends AsyncTask<String, Void, Integer> {

    private final String TAG = "RespontRequestTask";

    private Context context;
    private Long id;
    private Long requesterid;
    private Boolean accepted;

    public RespondRequestResponse delegate = null;

    public RespondRequestTask(Context context, Long id, Long requesterid, boolean accepted) {
        this.context = context;
        this.id = id;
        this.requesterid = requesterid;
        this.accepted = accepted;
    }

    @Override
    protected Integer doInBackground(String... params) {
        return send();
    }

    @Override
    protected void onPostExecute(Integer code) {
        super.onPostExecute(code);
        delegate.onTaskFinished(code);
    }

    private Integer send() {
        URL url = null;
        InputStream is = null;
        Integer result = null;
        try {
            url = new URL(context.getString(R.string.rest_url_acceptRequest));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "*/*");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            RespondRequestJSON reqjson = new RespondRequestJSON(this.id, this.requesterid, this.accepted);

            Log.d(TAG, reqjson.toString());

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(reqjson.toString());
            writer.close();
            os.close();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "Request OK");
            } else {
                Log.d(TAG, "Error occured during request");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "RequestPalTask ended!");
        return result;
    }
}
