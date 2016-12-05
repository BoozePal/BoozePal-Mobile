package hu.boozepalmobile.boozepal.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toolbar;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.activities.MainActivity;
import hu.boozepalmobile.boozepal.activities.RequestActivity;
import hu.boozepalmobile.boozepal.models.Pub;
import hu.boozepalmobile.boozepal.models.User;
import hu.boozepalmobile.boozepal.network.getpubs.GetPubTaskResponse;
import hu.boozepalmobile.boozepal.network.getpubs.GetPubsTask;
import hu.boozepalmobile.boozepal.network.requestpal.RequestPalResponse;
import hu.boozepalmobile.boozepal.utils.CalendarDecorator;

public class PalDetailFragment extends Fragment implements RequestPalResponse, GetPubTaskResponse {

    private final String TAG = "PalDetailFragment";

    private User userData;
    private User loggedUser;
    private String token;

    private TextView NameView;
    private ListView BoozeListView;
    private ListView PubListView;
    private RatingBar ratingBar;
    private MaterialCalendarView CalendarView;
    private CollapsingToolbarLayout appBarLayout;
    public Toolbar toolbar;

    public ArrayList<Pub> pubList;

    public PalDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = this.getActivity();
        appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_collapsing_detail);

        if (getArguments().containsKey("USER_DATA")) {
            userData = getArguments().getParcelable("USER_DATA");
            if (appBarLayout != null) {
                appBarLayout.setTitle(userData.getUsername());
            }
            loggedUser = getArguments().getParcelable("LOGGED_USER_DATA");
            token = getArguments().getString("TOKEN");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pal_detail, container, false);

        NameView = (TextView) rootView.findViewById(R.id.NameText);
        BoozeListView = (ListView) rootView.findViewById(R.id.mypal_boozelist);
        PubListView = (ListView) rootView.findViewById(R.id.mypal_publist);
        ratingBar = (RatingBar) rootView.findViewById(R.id.mypal_detail_price);
        CalendarView = (MaterialCalendarView) rootView.findViewById(R.id.detail_calendar_table);
        CalendarView.setTileWidth(rootView.getWidth() / 7);
        CalendarView.setTileHeight(rootView.getWidth() / 7);
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        CalendarView.state().edit().setMinimumDate(CalendarDay.from(today))
                .commit();

        if (userData != null) {
            NameView.setText(userData.getUsername());

            final ArrayAdapter BoozeAdapter = new ArrayAdapter(getActivity(),
                    android.R.layout.simple_list_item_1, userData.getFavouriteDrinks());
            BoozeListView.setAdapter(BoozeAdapter);
            final ArrayAdapter PubAdapter = new ArrayAdapter(getActivity(),
                    android.R.layout.simple_list_item_1, userData.getFavouritePub());
            PubListView.setAdapter(PubAdapter);

            PubListView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

            BoozeListView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

            ratingBar.setStepSize(1);
            ratingBar.setRating(userData.getPriceCategory());

            ArrayList<CalendarDay> calendarDays = new ArrayList<>();
            for (Date d : userData.getTimeBoard()) {
                calendarDays.add(CalendarDay.from(d));
            }
            CalendarView.addDecorator(new CalendarDecorator(Color.BLUE, calendarDays));

            CalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                @Override
                public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                    if (selected) {
                        Intent intent = new Intent(PalDetailFragment.this.getContext(), RequestActivity.class);
                        intent.putExtra("USER_DATA", PalDetailFragment.this.userData);
                        intent.putExtra("LOGGED_USER_DATA", PalDetailFragment.this.loggedUser);
                        intent.putExtra("TOKEN", PalDetailFragment.this.token);
                        intent.putExtra("PUBLIST", PalDetailFragment.this.pubList);
                        intent.putExtra("SELECTED_DATE", date);
                        startActivity(intent);
                    }
                }
            });
        }

        GetPubsTask gTask = new GetPubsTask(getContext());
        gTask.delegate = this;
        gTask.execute();

        return rootView;
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
