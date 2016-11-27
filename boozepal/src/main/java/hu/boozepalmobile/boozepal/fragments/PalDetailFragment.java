package hu.boozepalmobile.boozepal.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.models.User;
import hu.boozepalmobile.boozepal.utils.CalendarDecorator;

public class PalDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";

    private User userData;
    private User loggedUser;

    public TextView NameView;
    public TextView GenderView;
    public ListView BoozeListView;
    public ListView PubListView;
    public MaterialCalendarView CalendarView;
    public Toolbar toolbar;
    public ImageButton saveButton;

    public CalendarDay selectedDay;

    public PalDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);

        if(getArguments().containsKey("USER_DATA")){
            userData = (User) getArguments().getParcelable("USER_DATA");
            if (appBarLayout != null) {
                appBarLayout.setTitle(userData.getName());
            }
            loggedUser = (User) getArguments().getParcelable("LOGGED_USER_DATA");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pal_detail, container, false);

        saveButton = (ImageButton) rootView.findViewById(R.id.pal_request_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PalDetailFragment.this.selectedDay != null){

                }
            }
        });

        NameView = (TextView) rootView.findViewById(R.id.NameText);
        BoozeListView = (ListView) rootView.findViewById(R.id.BoozeListView);
        PubListView = (ListView) rootView.findViewById(R.id.PubListView);
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
            NameView.setText(userData.getName());

            final ArrayAdapter BoozeAdapter = new ArrayAdapter(getActivity(),
                    android.R.layout.simple_list_item_1, userData.getBoozes());
            BoozeListView.setAdapter(BoozeAdapter);
            final ArrayAdapter PubAdapter = new ArrayAdapter(getActivity(),
                    android.R.layout.simple_list_item_1, userData.getPubs());
            PubListView.setAdapter(PubAdapter);

            ArrayList<CalendarDay> calendarDays = new ArrayList<>();
            for(Date d : userData.getSavedDates()){
                calendarDays.add(CalendarDay.from(d));
            }
            CalendarView.addDecorator(new CalendarDecorator(Color.BLUE,calendarDays));

            CalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                @Override
                public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                    if(selected){
                        selectedDay = date;
                    }
                    else{
                        selectedDay = null;
                    }
                }
            });
        }

        return rootView;
    }
}
