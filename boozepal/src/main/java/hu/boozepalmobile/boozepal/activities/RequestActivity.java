package hu.boozepalmobile.boozepal.activities;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.models.Pub;
import hu.boozepalmobile.boozepal.models.User;
import hu.boozepalmobile.boozepal.network.requestpal.RequestPalResponse;
import hu.boozepalmobile.boozepal.network.requestpal.RequestPalTask;

public class RequestActivity extends AppCompatActivity implements RequestPalResponse {

    private final String TAG = "RequestActivity";

    private User loggedUser;
    private User user;
    private String token;

    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private ImageButton saveButton;
    private Spinner pubSpinner;
    private TimePicker timePicker;

    private CalendarDay selectedDay;

    public ArrayList<Pub> pubList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            user = b.getParcelable("USER_DATA");
            loggedUser = b.getParcelable("LOGGED_USER_DATA");
            token = b.getString("TOKEN");
            pubList = b.getParcelableArrayList("PUBLIST");
            selectedDay = b.getParcelable("SELECTED_DATE");
        }

        setUpView();
    }

    private void setUpView() {
        this.appBarLayout = (AppBarLayout) findViewById(R.id.request_appbar);
        this.toolbar = (Toolbar) appBarLayout.findViewById(R.id.request_toolbar);
        this.saveButton = (ImageButton) toolbar.findViewById(R.id.pal_request_button);
        this.pubSpinner = (Spinner) findViewById(R.id.select_pub_spinner);
        this.timePicker = (TimePicker) findViewById(R.id.select_time);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Touched back button!");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("USER_DATA", RequestActivity.this.loggedUser);
                intent.putExtra("TOKEN", token);
                startActivity(intent);
            }
        });

        ArrayAdapter<Pub> pubAdapter = new ArrayAdapter<>(this.getApplicationContext(), android.R.layout.simple_spinner_item, pubList);
        pubSpinner.setAdapter(pubAdapter);

        this.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = null;
                if (Build.VERSION.SDK_INT < 23) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(RequestActivity.this.selectedDay.getDate());
                    cal.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    cal.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    date = cal.getTime();

                } else {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(RequestActivity.this.selectedDay.getDate());
                    cal.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    cal.set(Calendar.MINUTE, timePicker.getMinute());
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    date = cal.getTime();
                }

                if ((RequestActivity.this.selectedDay != null)) {
                    Log.d(TAG, "Request started");
                    RequestPalTask rTask = new RequestPalTask(getApplicationContext(), date, RequestActivity.this.pubList.get(pubSpinner.getSelectedItemPosition()));
                    rTask.delegate = RequestActivity.this;
                    rTask.execute(RequestActivity.this.loggedUser, RequestActivity.this.user);
                }
            }
        });
    }

    @Override
    public void onTaskFinished(User result) {
        this.loggedUser = result;
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        intent.putExtra("USER_DATA", loggedUser);
        intent.putExtra("TOKEN", this.token);
        startActivity(intent);
    }
}
