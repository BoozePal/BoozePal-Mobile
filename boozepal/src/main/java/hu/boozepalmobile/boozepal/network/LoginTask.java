package hu.boozepalmobile.boozepal.network;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.application.BoozePalApplication;
import hu.boozepalmobile.boozepal.models.Coordinate;
import hu.boozepalmobile.boozepal.models.Drink;
import hu.boozepalmobile.boozepal.models.RemoteUser;
import hu.boozepalmobile.boozepal.models.User;
import hu.boozepalmobile.boozepal.utils.BoozePalLocation;
import hu.boozepalmobile.boozepal.utils.FindPalsJSON;

/**
 * Created by fanny on 2016.11.26..
 */

public class LoginTask extends AsyncTask<String, Void, User> {

    private final String TAG = "LoginTask";

    public LoginResponse delegate = null;

    private Context context;

    public LoginTask(Context context) {
        this.context = context;
    }

    @Override
    protected User doInBackground(String... params) {
        //User user = login(params[0]);
        User user = authenticate(params[0]);
        return user;
    }

    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);
        delegate.onTaskFinished(user);
    }

    private User login(String token) {
        URL url = null;
        InputStream is = null;
        User user = new User();
        try {
            url = new URL(context.getString(R.string.rest_url_login));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "*/*");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            Gson gson = new Gson();
            gson.toJson(token);

            Log.d(TAG, gson.toString());

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(gson.toString());
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

            Gson gsonuser = new Gson();
            user = gsonuser.fromJson(sb.toString(), User.class);

            Log.d(TAG, user.toString());

            /*JSONArray jsonResult = new JSONArray(result);

            for (int i = 0; i < jsonResult.length(); i++)
            {
                JSONObject jsonObj = jsonResult.getJSONObject(i);

                List<String> favouriteBoozes = new ArrayList<>();
                if(!jsonObj.isNull("favouriteDrink")) {
                    JSONArray boozes = jsonObj.getJSONArray("favouriteDrink");
                    for (int j = 0; j < boozes.length(); ++j) {
                        JSONObject booze = boozes.getJSONObject(j);
                        Iterator<?> keys = booze.keys();

                        while (keys.hasNext()) {
                            String key = (String) keys.next();
                            //favouriteBoozes.add((String) booze.get(key));
                            favouriteBoozes.add(key);
                        }
                    }
                }

                List<String> favouritePubs = new ArrayList<>();
                if(!jsonObj.isNull("favouritePub")) {
                    JSONArray pubs = jsonObj.getJSONArray("favouritePub");
                    for (int j = 0; j < pubs.length(); ++j) {
                        JSONObject pub = pubs.getJSONObject(j);
                        Iterator<?> keys = pub.keys();

                        while (keys.hasNext()) {
                            String key = (String) keys.next();
                            //favouritePubs.add((String) booze.get(key));
                            favouritePubs.add(key);
                        }
                    }
                }

                List<Date> savedDates = new ArrayList<>();
                if(!jsonObj.isNull("savedDates")) {
                    JSONArray dates = jsonObj.getJSONArray("savedDates");
                    for (int j = 0; j < dates.length(); ++j) {
                        JSONObject date = dates.getJSONObject(j);
                        Iterator<?> keys = date.keys();

                        while (keys.hasNext()) {
                            DateFormat format = new SimpleDateFormat("MMMM d yyyy", Locale.ENGLISH);
                            Date key = format.parse(keys.next().toString());
                            //favouritePubs.add((String) booze.get(key));
                            savedDates.add(key);
                        }
                    }
                }

                String name = "";
                if(!jsonObj.isNull("username"))
                    name = jsonObj.getString("username");

                String city = "";
                if(!jsonObj.isNull("address")){
                    JSONObject address = new JSONObject(jsonObj.getString("address"));
                    city = address.getString("town");
                }

                Long id = null;
                if(!jsonObj.isNull("id"))
                    id = jsonObj.getLong("id");

                int radius = 10;
                if(!jsonObj.isNull("searchRadius"))
                    radius = Integer.parseInt(jsonObj.getString("searchRadius"));

                int priceCategory = 1;
                if(!jsonObj.isNull("priceCategory"))
                    priceCategory = Integer.parseInt(jsonObj.getString("priceCategory"));

                System.out.println(jsonObj);
                User currentuser = new User(id, name, city, favouriteBoozes, favouritePubs, radius, priceCategory, savedDates, new ArrayList<User>(), null);
                users.add(currentuser);
            }*/

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return user;

    }

    private User authenticate(String token) {
        URL url = null;
        InputStream is = null;
        User result = null;
        try {
            url = new URL(context.getString(R.string.rest_url_login));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "*/*");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            JSONObject sendObj = new JSONObject();
            sendObj.put("token", token);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(sendObj.toString());
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

            Log.d(TAG,sb.toString());

            RemoteUser remoteUser = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            }).create().fromJson(sb.toString(),RemoteUser.class);

            /*JSONObject obj = new JSONObject(sb.toString());

            if(obj.length() == 0){
                Log.v(TAG, "Error occured during login!");
                return null;
            }

            List<Drink> favouriteBoozes = new ArrayList<>();
            if(!obj.isNull("favouriteDrink")) {*/
                /*JSONArray boozes = obj.getJSONArray("favouriteDrink");
                for (int i = 0; i < boozes.length(); ++i) {
                    JSONObject booze = boozes.getJSONObject(i);
                    Iterator<?> keys = booze.keys();

                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        //favouriteBoozes.add((String) booze.get(key));
                        favouriteBoozes.add(key);
                    }
                }*/
            /*}

            List<String> favouritePubs = new ArrayList<>();
            if(!obj.isNull("favouritePub")) {
                JSONArray pubs = obj.getJSONArray("favouritePub");
                for (int i = 0; i < pubs.length(); ++i) {
                    JSONObject pub = pubs.getJSONObject(i);
                    Iterator<?> keys = pub.keys();

                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        //favouritePubs.add((String) booze.get(key));
                        favouritePubs.add(key);
                    }
                }
            }

            List<Date> savedDates = new ArrayList<>();
            if(!obj.isNull("timeBoard")) {
                JSONArray dates = obj.getJSONArray("timeBoard");
                for (int i = 0; i < dates.length(); ++i) {
                    Long date = dates.getLong(i);
                    //Long date = Long.valueOf(dates.getJSONObject(i).toString());
                    //Iterator<?> keys = date.keys();

                    // while (keys.hasNext()) {
                    //System.out.println("time: " + date);
                    //DateFormat df = new SimpleDateFormat("MMM d, yyyy HH:mm:ss aa", Locale.ENGLISH);
                    //Date key = df.parse(date.toString());
                    Date d = new Date(date);
                    //favouritePubs.add((String) booze.get(key));
                    savedDates.add(d);
                    //}
                }
            }

            List<User> myPals = new ArrayList<>();
            if(!obj.isNull("myPals")) {*/
                /*JSONArray mypals = obj.getJSONArray("myPals");
                    for (int i = 0; i < mypals.length(); ++i) {
                        JSONObject date = mypals.getJSONObject(i);
                        Iterator<?> keys = date.keys();

                        while (keys.hasNext()) {
                            DateFormat format = new SimpleDateFormat("MMMM d yyyy", Locale.ENGLISH);
                            Date key = format.parse(keys.next().toString());
                            //favouritePubs.add((String) booze.get(key));
                            savedDates.add(key);
                        }
                    }
            }


            String name = "";
            if(!obj.isNull("username"))
                name = obj.getString("username");

            String city = "";
            if(!obj.isNull("address ")){
                JSONObject address = new JSONObject(obj.getString("address"));
                city = address.getString("town");
            }

            Long id = null;
            if(!obj.isNull("id"))
                id = obj.getLong("id");

            int radius = 10;
            if(!obj.isNull("searchRadius"))
                radius = Integer.parseInt(obj.getString("searchRadius"));

            int priceCategory = 1;
            if(!obj.isNull("priceCategory"))
                priceCategory = Integer.parseInt(obj.getString("priceCategory"));*/

            //User user = new User(id, name, city, favouriteBoozes, favouritePubs, radius, priceCategory, savedDates, myPals, null);
            //user.setToken(token);

            User user = new User(remoteUser.getId(), remoteUser.getUsername(), remoteUser.getAddress().getTown(), remoteUser.getFavouriteDrinks(), remoteUser.getFavouritePub(), remoteUser.getTimeBoard(), remoteUser.getSearchRadius(), remoteUser.getPriceCategory(), remoteUser.getLastKnownCoordinate(), remoteUser.getActualPals());
            Log.d(TAG, user.toString());

            result = user;

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

