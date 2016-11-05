package hu.boozepalmobile.boozepal;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

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

import hu.boozepalmobile.boozepal.User.User;

public class SettingsActivity extends AppCompatActivity {

    private User user;

    private EditText editName;
    private EditText editCity;
    private RatingBar editPrice;
    private SeekBar editRadius;
    private TextView labelRadius;

    private Button saveSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_main);

        Bundle b = getIntent().getExtras();
        if (b != null)
            user = b.getParcelable("USER_DATA");

        if (user != null) {
            setupView();
        }

        setupButton();
    }

    private void setupButton() {
        saveSettingsButton = (Button) findViewById(R.id.edit_settings_button);
        saveSettingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SaveSettingsTask saveTask = new SaveSettingsTask();
                saveTask.execute(user);
            }
        });
    }

    void setupView() {
        editName = (EditText) findViewById(R.id.settings_edit_name);
        editName.setText(user.getName());

        editCity = (EditText) findViewById(R.id.settings_edit_city);
        editCity.setText(user.getCity());

        editPrice = (RatingBar) findViewById(R.id.settings_edit_price);
        editPrice.setMax(5);
        editPrice.setStepSize(1);
        editPrice.setRating(user.getPriceCategory());

        labelRadius = (TextView) findViewById(R.id.settings_label_km);

        editRadius = (SeekBar) findViewById(R.id.settings_edit_radius);
        editRadius.setProgress(user.getSearchRadius());
        System.out.println(user.getSearchRadius());
        editRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                labelRadius.setText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                labelRadius.setText(seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        String km = editRadius.getProgress() + " km";
        labelRadius.setText(km);

    }

    private class SaveSettingsTask extends AsyncTask<User, Void, String> {


        @Override
        protected String doInBackground(User... params) {
            String success = saveSettings(params[0]);
            return null;
        }

        protected void onPostExecute(String s) {

            //String result = null;
            // JSONObject obj = new JSONObject(s);
        }

        private String saveSettings(User user) {
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
                obj.put("token", user.getToken());
                obj.put("fullName", user.getName());
                obj.put("city", user.getCity());
                obj.put("radius", user.getSearchRadius());
                obj.put("priceCategory", user.getPriceCategory());
                obj.put("boozes", user.getBoozes());
                obj.put("pubs", user.getPubs());

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(obj.toString());
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
                result = sb.toString();

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
