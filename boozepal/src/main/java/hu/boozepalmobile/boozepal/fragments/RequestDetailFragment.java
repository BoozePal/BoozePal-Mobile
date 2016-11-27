package hu.boozepalmobile.boozepal.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.models.User;
import hu.boozepalmobile.boozepal.network.RequestPalResponse;
import hu.boozepalmobile.boozepal.utils.CalendarDecorator;

public class RequestDetailFragment extends Fragment{
    private User userData;
    private User loggedUser;

    public TextView NameView;
    public ListView BoozeListView;
    public ListView PubListView;

    public RequestDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_collapsing_requestdetail);

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
        NameView = (TextView) rootView.findViewById(R.id.NameText);
        BoozeListView = (ListView) rootView.findViewById(R.id.BoozeListView);
        PubListView = (ListView) rootView.findViewById(R.id.PubListView);

        if (userData != null) {
            NameView.setText(userData.getName());

            final ArrayAdapter BoozeAdapter = new ArrayAdapter(getActivity(),
                    android.R.layout.simple_list_item_1, userData.getBoozes());
            BoozeListView.setAdapter(BoozeAdapter);
            final ArrayAdapter PubAdapter = new ArrayAdapter(getActivity(),
                    android.R.layout.simple_list_item_1, userData.getPubs());
            PubListView.setAdapter(PubAdapter);
        }

        return rootView;
    }

}
