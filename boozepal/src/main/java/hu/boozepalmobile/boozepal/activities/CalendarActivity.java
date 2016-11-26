package hu.boozepalmobile.boozepal.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

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
import java.util.Date;
import java.util.List;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.models.User;

public class CalendarActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private ImageButton saveButton;

    private List<Date> selectedDates;

    private String token;
    private User user;
    private User modifiedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        final ViewGroup rv = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        selectedDates = new ArrayList<>();

        Bundle b = getIntent().getExtras();
        if (b != null) {
            user = b.getParcelable("USER_DATA");
            modifiedUser = b.getParcelable("USER_DATA");
            token = b.getString("TOKEN");
        }

        calendarView = (MaterialCalendarView) findViewById(R.id.calendar_table);
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);

        for(Date d: user.getSavedDates()){
            selectedDates.add(d);
            calendarView.setDateSelected(d,true);
        }

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if (selected) {
                    selectedDates.add(date.getDate());
                } else {
                    selectedDates.remove(date.getDate());
                }
            }
        });


        calendarView.setTileWidth(rv.getWidth()/7);
        calendarView.setTileHeight(rv.getWidth()/7);

        setupToolbar();
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.calendar_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CalendarActivity","Touched back button!");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("USER_DATA", user);
                intent.putExtra("TOKEN", token);
                startActivity(intent);
            }
        });

        saveButton = (ImageButton) findViewById(R.id.calendar_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveCalendarTask saveTask = new SaveCalendarTask();
                saveTask.execute(token);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("USER_DATA", user);
                intent.putExtra("TOKEN", token);
                startActivity(intent);
            }
        });
    }

    private class SaveCalendarTask extends AsyncTask<String, Void, String> {

        private String token;

        @Override
        protected String doInBackground(String... params) {
            token = params[0];
            String result = save(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println(s);

            //super.onPostExecute(s);
        }

        private String save(String token) {
            URL url = null;
            InputStream is = null;
            String result = null;
            try {
                url = new URL(getString(R.string.rest_url_settings));

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
                obj.put("token", CalendarActivity.this.token);
                obj.put("user", user);


                System.out.println(obj.toString());

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(obj.toString());
                writer.close();
                os.close();

                System.out.println(conn.getResponseMessage());

                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    Log.d("SettingsActivity", "Saving OK");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("USER_DATA", modifiedUser);
                    intent.putExtra("TOKEN", token);
                    startActivity(intent);
                }
                else{
                    Log.d("SettingsActivity", "Error occured during saving");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("USER_DATA", user);
                    intent.putExtra("TOKEN", token);
                    startActivity(intent);
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

}
