package hu.boozepalmobile.boozepal.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.activities.MainActivity;
import hu.boozepalmobile.boozepal.models.Pub;
import hu.boozepalmobile.boozepal.models.User;
import hu.boozepalmobile.boozepal.network.getpubs.GetPubTaskResponse;
import hu.boozepalmobile.boozepal.network.getpubs.GetPubsTask;
import hu.boozepalmobile.boozepal.network.requestpal.RequestPalResponse;
import hu.boozepalmobile.boozepal.network.requestpal.RequestPalTask;
import hu.boozepalmobile.boozepal.utils.CalendarDecorator;

public class PalDetailFragment extends Fragment implements RequestPalResponse, GetPubTaskResponse{
    private final String TAG = "PalDetailFragment";

    private User userData;
    private User loggedUser;
    private String token;

    public TextView NameView;
    public ListView BoozeListView;
    public ListView PubListView;
    private RatingBar ratingBar;
    public MaterialCalendarView CalendarView;
    private CollapsingToolbarLayout appBarLayout;
    public Toolbar toolbar;
    public ImageButton saveButton;
    private Spinner pubSpinner;

    public CalendarDay selectedDay;
    public Pub selectedPub;

    public ArrayList<Pub> pubList;

    public PalDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = this.getActivity();
        appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_collapsing_detail);

        if(getArguments().containsKey("USER_DATA")){
            userData = (User) getArguments().getParcelable("USER_DATA");
            if (appBarLayout != null) {
                appBarLayout.setTitle(userData.getUsername());
            }
            loggedUser = (User) getArguments().getParcelable("LOGGED_USER_DATA");
            token = getArguments().getString("TOKEN");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pal_detail, container, false);

        saveButton = (ImageButton) appBarLayout.findViewById(R.id.pal_request_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Touched button!" + PalDetailFragment.this.selectedPub.toString());
                if((PalDetailFragment.this.selectedDay != null) && (PalDetailFragment.this.selectedPub != null)){
                    RequestPalTask rTask = new RequestPalTask(getContext(),PalDetailFragment.this.selectedDay.getDate(), PalDetailFragment.this.selectedPub);
                    rTask.delegate = PalDetailFragment.this;
                    rTask.execute(PalDetailFragment.this.loggedUser, PalDetailFragment.this.userData);
                }
            }
        });

        NameView = (TextView) rootView.findViewById(R.id.NameText);
        BoozeListView = (ListView) rootView.findViewById(R.id.BoozeListView);
        PubListView = (ListView) rootView.findViewById(R.id.PubListView);
        ratingBar = (RatingBar) rootView.findViewById(R.id.pal_detail_price);
        CalendarView = (MaterialCalendarView) rootView.findViewById(R.id.detail_calendar_table);
        CalendarView.setTileWidth(rootView.getWidth()/7);
        CalendarView.setTileHeight(rootView.getWidth()/7);
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY,0);
        //Calendar maxDate = Calendar.getInstance();
        //maxDate.set(Calendar.MONTH,today.get(Calendar.MONTH) + 3);
        CalendarView.state().edit().setMinimumDate(CalendarDay.from(today))
                //.setMaximumDate(CalendarDay.from(maxDate))
                .commit();

        if (userData != null) {
            NameView.setText(userData.getUsername());

            final ArrayAdapter BoozeAdapter = new ArrayAdapter(getActivity(),
                    android.R.layout.simple_list_item_1, userData.getFavouriteDrinks());
            BoozeListView.setAdapter(BoozeAdapter);
            final ArrayAdapter PubAdapter = new ArrayAdapter(getActivity(),
                    android.R.layout.simple_list_item_1, userData.getFavouritePub());
            PubListView.setAdapter(PubAdapter);

            ratingBar.setStepSize(1);
            ratingBar.setRating(userData.getPriceCategory());

            ArrayList<CalendarDay> calendarDays = new ArrayList<>();
            for(Date d : userData.getTimeBoard()){
                calendarDays.add(CalendarDay.from(d));
            }
            CalendarView.addDecorator(new CalendarDecorator(Color.BLUE,calendarDays));

            CalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                @Override
                public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                    if(selected){
                        showDialog();
                        selectedDay = date;
                    }
                    else{
                        selectedDay = null;
                    }
                }
            });
        }

        GetPubsTask gTask = new GetPubsTask(getContext());
        gTask.delegate = this;
        gTask.execute();

        return rootView;
    }

    private void showDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.select_pub_dialog, null);

        pubSpinner = (Spinner) dialogView.findViewById(R.id.select_pub_spinner);

        ArrayAdapter<Pub> pubAdapter = new ArrayAdapter<Pub>(getContext(), android.R.layout.simple_spinner_item, pubList);
        pubSpinner.setAdapter(pubAdapter);

        pubSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPub = pubList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPub = null;
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Select pub!");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

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
    public void onTaskFinished(User result) {
        this.loggedUser = result;
        Intent intent = new Intent(this.getContext(), MainActivity.class);
        intent.putExtra("USER_DATA", loggedUser);
        intent.putExtra("TOKEN", this.token);
        startActivity(intent);
    }

    @Override
    public void onTaskFinished(ArrayList<Pub> pubs) {
        this.pubList = pubs;
    }
}
