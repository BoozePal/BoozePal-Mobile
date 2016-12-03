package hu.boozepalmobile.boozepal.network.getdrink;

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
import java.util.HashMap;
import java.util.List;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.models.Drink;
import hu.boozepalmobile.boozepal.models.DrinkTypeEnum;

/**
 * Created by fanny on 2016.11.29..
 */

public class GetDrinkTask extends AsyncTask<String,Void,HashMap<DrinkTypeEnum, List<Drink>>> {

    private final String TAG = "GetDrinkTask";

    private Context context;

    public GetDrinkTaskResponse delegate = null;

    public GetDrinkTask(Context context) {
        this.context = context;
    }

    @Override
    protected HashMap<DrinkTypeEnum, List<Drink>> doInBackground(String... params) {
        return getDrinks();
    }

    @Override
    protected void onPostExecute(HashMap<DrinkTypeEnum, List<Drink>> drinkTypes) {
        super.onPostExecute(drinkTypes);
        delegate.onTaskFinished(drinkTypes);
    }

    private HashMap<DrinkTypeEnum, List<Drink>> getDrinks(){
        URL url = null;
        InputStream is = null;
        HashMap<DrinkTypeEnum,List<Drink>> drinkTypes = new HashMap<>();
        //List<DrinkType> drinkTypes = new ArrayList<>();
        try {
            url = new URL(context.getString(R.string.rest_url_getdrinksall));

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

            Type listType = new TypeToken<List<Drink>>() {}.getType();

            Gson gson = new Gson();
            List<Drink> drinks = gson.fromJson(sb.toString(), listType);

            for(Drink d : drinks){
                url = new URL(context.getString(R.string.rest_url_getdrinktype) + d.getDrinkType());
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                line = null;
                sb = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                br.close();

                Log.d(TAG, sb.toString());

                //Gson gsondrink = new Gson();
                //Drink drink = gsondrink.fromJson(sb.toString(), Drink.class);;

                Gson gsondrink = new Gson();
                List<Drink> typedrinks = gson.fromJson(sb.toString(), listType);

                if(!typedrinks.isEmpty()){
                    if(drinkTypes.containsKey(typedrinks.get(0))){
                        continue;
                    }
                }

                for(Drink drink : typedrinks){
                    System.out.println("drink:" + drink.toString());
                    //Gson drinkgson = new Gson();
                    //Drink drink = drinkgson.fromJson(sb.toString(), Drink.class);;

                    if(drinkTypes.containsKey(drink.getDrinkType())){
                        List<Drink> drinkList = drinkTypes.get(drink.getDrinkType());
                        drinkList.add(drink);
                        drinkTypes.put(drink.getDrinkType(),drinkList);
                    }
                    else{
                        ArrayList<Drink> drinklist = new ArrayList<>();
                        drinklist.add(drink);
                        drinkTypes.put(drink.getDrinkType(),drinklist);
                    }

                }

            }

            System.out.println("map " + drinkTypes);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return drinkTypes;
    }
}
