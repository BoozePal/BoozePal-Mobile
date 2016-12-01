package hu.boozepalmobile.boozepal.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.models.Drink;
import hu.boozepalmobile.boozepal.models.DrinkType;
import hu.boozepalmobile.boozepal.models.DrinkTypeEnum;
import hu.boozepalmobile.boozepal.models.User;
import hu.boozepalmobile.boozepal.network.GetDrinkTask;
import hu.boozepalmobile.boozepal.network.GetDrinkTaskResponse;
import hu.boozepalmobile.boozepal.utils.UpdateUserJSON;

public class SettingsActivity extends AppCompatActivity implements GetDrinkTaskResponse {

    private User user;
    private User modifiedUser;

    private String token;

    private EditText editName;
    private EditText editCity;
    private RatingBar editPrice;
    private SeekBar editRadius;
    private TextView labelRadius;
    private ListView boozeListView;
    private ListView pubListView;
    private ImageButton addBooze;
    private ImageButton addPub;
    private Spinner typeSpinner;
    private Spinner drinkSpinner;

    private List<String> drinktype;
    private List<String> drink;

    private HashMap<DrinkTypeEnum, List<Drink>> drinks;
    private List<DrinkTypeEnum> types;

    private Button saveSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SettingsActivity", "Touched back button!");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("USER_DATA", modifiedUser);
                intent.putExtra("TOKEN", token);
                startActivity(intent);
            }
        });

        Bundle b = getIntent().getExtras();
        if (b != null) {
            user = b.getParcelable("USER_DATA");
            modifiedUser = b.getParcelable("USER_DATA");
            token = b.getString("TOKEN");
        }

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
                Log.d("SettingActivity", "Sav Changed button touched!");
                SaveSettingsTask saveTask = new SaveSettingsTask();
                saveTask.execute(user);
            }
        });
    }

    void setupView() {
        editName = (EditText) findViewById(R.id.settings_edit_name);
        editName.setSelectAllOnFocus(true);
        editName.setText(user.getName());
        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                modifiedUser.setName(String.valueOf(s.toString()));
            }
        });

        editCity = (EditText) findViewById(R.id.settings_edit_city);
        editCity.setSelectAllOnFocus(true);
        editCity.setText(user.getCity());
        editCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                modifiedUser.setCity(String.valueOf(s.toString()));
            }
        });

        editPrice = (RatingBar) findViewById(R.id.settings_edit_price);
        editPrice.setMax(4);
        editPrice.setStepSize(1);
        editPrice.setRating(user.getPriceCategory());
        editPrice.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                modifiedUser.setPriceCategory((int) rating);
            }
        });

        labelRadius = (TextView) findViewById(R.id.settings_label_km);

        editRadius = (SeekBar) findViewById(R.id.settings_edit_radius);
        editRadius.setProgress(user.getSearchRadius());
        System.out.println(user.getSearchRadius());
        editRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String km = seekBar.getProgress() + " km";
                labelRadius.setText(km);
                modifiedUser.setSearchRadius(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                String km = seekBar.getProgress() + " km";
                labelRadius.setText(km);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        String km = editRadius.getProgress() + " km";
        labelRadius.setText(km);

        boozeListView = (ListView) findViewById(R.id.settings_boozelist);
        pubListView = (ListView) findViewById(R.id.settings_publist);

        final ArrayAdapter BoozeAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, user.getBoozes());
        boozeListView.setAdapter(BoozeAdapter);
        final ArrayAdapter PubAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, user.getPubs());
        pubListView.setAdapter(PubAdapter);

        addBooze = (ImageButton) findViewById(R.id.settings_add_booze);
        addPub = (ImageButton) findViewById(R.id.settings_add_pub);

        addBooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddBoozeDialog();
            }
        });

        addPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPubDialog();
            }
        });

        GetDrinkTask dt = new GetDrinkTask(getApplicationContext());
        dt.delegate = this;
        dt.execute();
    }

    private void showAddBoozeDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_booze_dialog, null);
        dialogBuilder.setView(dialogView);

        //final EditText editBooze = (EditText) dialogView.findViewById(R.id.settings_add_list_edit);
        typeSpinner = (Spinner) dialogView.findViewById(R.id.settings_drinktype_spinner);
        drinkSpinner = (Spinner) dialogView.findViewById(R.id.settings_drink_spinner);

        types = new ArrayList<>(this.drinks.keySet());
        ArrayAdapter<DrinkTypeEnum> drinktypeAdapter = new ArrayAdapter<DrinkTypeEnum>(this, android.R.layout.simple_spinner_item, types);
        typeSpinner.setAdapter(drinktypeAdapter);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<String> names = new ArrayList<String>();
                for (Drink d : SettingsActivity.this.drinks.get(SettingsActivity.this.types.get(position))) {
                    names.add(d.getName());
                }

                ArrayAdapter<String> drinkadapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, names);
                SettingsActivity.this.drinkSpinner.setAdapter(drinkadapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dialogBuilder.setTitle("Add new booze");
        dialogBuilder.setMessage("Enter the new booze");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.d("SettingsActivity", "Added new booze!");
                DrinkTypeEnum drinkEnum = DrinkTypeEnum.valueOf(typeSpinner.getSelectedItem().toString());
                int id = 0;
                for (Drink d : SettingsActivity.this.drinks.get(drinkEnum)) {
                    if (d.getName().equals(drinkSpinner.getSelectedItem().toString()))
                        id = d.getId();
                }
                modifiedUser.addBooze(new Drink(
                        id,
                        drinkSpinner.getSelectedItem().toString(),
                        drinkEnum));
                final ArrayAdapter BoozeAdapter = new ArrayAdapter(SettingsActivity.this,
                        android.R.layout.simple_list_item_1, modifiedUser.getBoozes());
                boozeListView.setAdapter(BoozeAdapter);
                //modifiedUser.addBooze(editBooze.getText().toString());
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void showAddPubDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.fragment_add_list_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.settings_add_list_edit);

        dialogBuilder.setTitle("Add new pub");
        dialogBuilder.setMessage("Enter the new pub");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.d("SettingsActivity", "Added new pub!");
                //modifiedUser.addPub(editText.getText().toString());
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    public void onTaskFinished(HashMap<DrinkTypeEnum, List<Drink>> result) {
        this.drinks = result;
        /*this.drinks = result;
        this.drinktype = new ArrayList<>();
        for(DrinkType d : result){
            this.drinktype.add(d.getDrinkType().getValue());
        }*/

    }

    private class SaveSettingsTask extends AsyncTask<User, Void, String> {


        @Override
        protected String doInBackground(User... params) {
            String success = saveSettings(params[0]);
            return null;
        }

        protected void onPostExecute(String s) {

        }

        private String saveSettings(User user) {
            URL url = null;
            InputStream is = null;
            String result = "";
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

                UpdateUserJSON gson = new UpdateUserJSON(SettingsActivity.this.token, SettingsActivity.this.modifiedUser);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(gson.toString());
                writer.close();
                os.close();

                System.out.println(conn.getResponseMessage());

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.d("SettingsActivity", "Saving OK");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("USER_DATA", modifiedUser);
                    intent.putExtra("TOKEN", token);
                    startActivity(intent);
                } else {
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
            }

            return result;
        }
    }
}
