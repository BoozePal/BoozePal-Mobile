package hu.boozepalmobile.boozepal.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

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
import hu.boozepalmobile.boozepal.models.Pub;
import hu.boozepalmobile.boozepal.models.RemoteUser;
import hu.boozepalmobile.boozepal.models.User;
import hu.boozepalmobile.boozepal.utils.BoozePalLocation;
import hu.boozepalmobile.boozepal.utils.FindPalsJSON;

/**
 * Created by fanny on 2016.11.26..
 */

public class FindPalsTask extends AsyncTask<User, Void, ArrayList<User>> {

    private final String TAG = "FindPalsTask";

    public FindPalsResponse delegate = null;

    private String token;
    private Context context;

    public FindPalsTask(Context context, String token) {
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

            FindPalsJSON js = new FindPalsJSON(this.token, user);

            System.out.println(user.toString());

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(js.toString());
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

            Log.d(TAG, sb.toString());

            Type listType = new TypeToken<List<RemoteUser>>() {
            }.getType();

            List<RemoteUser> remoteUsers = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            }).create().fromJson(sb.toString(), listType);

            for (RemoteUser remoteUser : remoteUsers) {
                User ruser = new User();
                if (remoteUser.getId() != null)
                    ruser.setId(remoteUser.getId());

                if (remoteUser.getAddress() != null)
                    ruser.setCity(remoteUser.getAddress().getTown());

                if (remoteUser.getUsername() != null)
                    ruser.setName(remoteUser.getUsername());

                if (remoteUser.getFavouriteDrinks().isEmpty())
                    ruser.setBoozes(new ArrayList<Drink>());
                else
                    ruser.setBoozes(remoteUser.getFavouriteDrinks());

                if (remoteUser.getFavouritePub().isEmpty())
                    ruser.setPubs(new ArrayList<Pub>());
                else
                    ruser.setPubs(remoteUser.getFavouritePub());

                ruser.setSearchRadius(remoteUser.getSearchRadius());

                ruser.setPriceCategory(remoteUser.getPriceCategory());

                if (remoteUser.getTimeBoard().isEmpty())
                    ruser.setSavedDates(new ArrayList<Date>());
                else
                    ruser.setSavedDates(remoteUser.getTimeBoard());

                Log.d(TAG, ruser.toString());
                users.add(ruser);

            }
            //users.add(new User(remoteUser.getId(), remoteUser.getUsername(), remoteUser.getAddress().getTown(), remoteUser.getFavouriteDrinks(), remoteUser.getFavouritePub(), remoteUser.getSearchRadius(), remoteUser.getPriceCategory(), remoteUser.getTimeBoard(), null, null));

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
                //User currentuser = new User(id, name, city, favouriteBoozes, favouritePubs, radius, priceCategory, savedDates, new ArrayList<User>(), null);
                //users.add(currentuser);
            }*/

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;

    }
}
