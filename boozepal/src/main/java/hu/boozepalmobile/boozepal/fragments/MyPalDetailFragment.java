package hu.boozepalmobile.boozepal.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.models.Drink;
import hu.boozepalmobile.boozepal.models.User;
import hu.boozepalmobile.boozepal.utils.UIPalRequest;

public class MyPalDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";

    private User loggedUser;
    private UIPalRequest request;
    private String token;

    private TextView nameView;
    private ListView boozeListView;
    private ListView pubListView;
    private TextView dateView;
    private TextView pubView;
    private RatingBar ratingBar;

    public MyPalDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_collapsing_mypaldetail);

        request = (UIPalRequest) getArguments().getParcelable("SELECTED_USER_DATA");
        if (appBarLayout != null) {
            appBarLayout.setTitle(request.getUser().getUsername());
        }
        loggedUser = (User) getArguments().getParcelable("LOGGED_USER_DATA");
        token = getArguments().getString("TOKEN");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mypal_detail, container, false);
        nameView = (TextView) rootView.findViewById(R.id.mypal_name);
        dateView = (TextView) rootView.findViewById(R.id.mypal_date_text);
        pubView = (TextView) rootView.findViewById(R.id.mypal_pub_text);
        boozeListView = (ListView) rootView.findViewById(R.id.mypal_boozelist);
        pubListView = (ListView) rootView.findViewById(R.id.mypal_publist);
        ratingBar = (RatingBar) rootView.findViewById(R.id.mypal_detail_price);

        if (request.getUser() != null) {
            User user = request.getUser();
            nameView.setText(user.getUsername());

            if(user.getFavouriteDrinks().isEmpty()){
                final ArrayAdapter BoozeAdapter = new ArrayAdapter(getActivity(),
                        android.R.layout.simple_list_item_1, Arrays.asList("Opps - user has no favourite drink :("));
                boozeListView.setAdapter(BoozeAdapter);
            }else{
                final ArrayAdapter BoozeAdapter = new ArrayAdapter(getActivity(),
                        android.R.layout.simple_list_item_1, user.getFavouriteDrinks());
                boozeListView.setAdapter(BoozeAdapter);
            }

            if(user.getFavouritePub().isEmpty()){
                final ArrayAdapter PubAdapter = new ArrayAdapter(getActivity(),
                        android.R.layout.simple_list_item_1, Arrays.asList("Oops - user has no favourite pub :("));
                pubListView.setAdapter(PubAdapter);
            }else{
                final ArrayAdapter PubAdapter = new ArrayAdapter(getActivity(),
                        android.R.layout.simple_list_item_1, user.getFavouritePub());
                pubListView.setAdapter(PubAdapter);
            }

            ratingBar.setRating(user.getPriceCategory());

            SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd - hh:mm");
            Date date = request.getDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            dateView.setText(format.format(calendar.getTime()));
            if(request.getPub() != null)
                pubView.setText(request.getPub().toString());
        }

        return rootView;
    }
}
