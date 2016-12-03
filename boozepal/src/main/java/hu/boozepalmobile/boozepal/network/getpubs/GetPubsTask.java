package hu.boozepalmobile.boozepal.network.getpubs;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.models.Pub;

/**
 * Created by fanny on 2016.12.01..
 */

public class GetPubsTask extends AsyncTask<String, Void, ArrayList<Pub>> {

    private final String TAG = "GetPubTask";

    private Context context;

    public GetPubTaskResponse delegate = null;

    public GetPubsTask(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<Pub> doInBackground(String... params) {
        return getPubs();
    }

    @Override
    protected void onPostExecute(ArrayList<Pub> pubTypes) {
        super.onPostExecute(pubTypes);
        delegate.onTaskFinished(pubTypes);
    }

    private ArrayList<Pub> getPubs() {
        URL url = null;
        InputStream is = null;
        //List<Pub> result = new ArrayList<>();
        ArrayList<Pub> result = new ArrayList<>();
        try {
            url = new URL(context.getString(R.string.rest_url_getpubs));

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

            Type listType = new TypeToken<List<Pub>>() {
            }.getType();

            Gson gson = new Gson();
            ArrayList<Pub> pubs = gson.fromJson(sb.toString(), listType);
            result = pubs;
            //for(Pub p: pubs)
            //    result.put(p.getName(),p);
            Log.d(TAG, pubs.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
