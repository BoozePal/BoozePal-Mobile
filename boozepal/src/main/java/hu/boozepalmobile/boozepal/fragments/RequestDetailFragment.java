package hu.boozepalmobile.boozepal.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.activities.MainActivity;
import hu.boozepalmobile.boozepal.models.User;
import hu.boozepalmobile.boozepal.network.getuser.GetUserResponse;
import hu.boozepalmobile.boozepal.network.getuser.GetUserTask;
import hu.boozepalmobile.boozepal.network.respontrequest.RespondRequestResponse;
import hu.boozepalmobile.boozepal.network.respontrequest.RespondRequestTask;
import hu.boozepalmobile.boozepal.utils.UIPalRequest;

public class RequestDetailFragment extends Fragment implements RespondRequestResponse, GetUserResponse {
    private UIPalRequest request;
    private String token;
    private User loggedUser;

    private TextView NameView;
    private ListView BoozeListView;
    private ListView PubListView;
    private TextView dateView;
    private TextView pubView;
    private RatingBar ratingBar;

    private FloatingActionButton acceptButton;
    private FloatingActionButton denyButton;

    public RequestDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_collapsing_requestdetail);

        acceptButton = (FloatingActionButton) activity.findViewById(R.id.accept_floating_button);
        denyButton = (FloatingActionButton) activity.findViewById(R.id.deny_floating_button);

        request = getArguments().getParcelable("SELECTED_REQUEST_DATA");
        if (appBarLayout != null) {
            appBarLayout.setTitle(request.getUser().getUsername());
        }
        loggedUser = getArguments().getParcelable("LOGGED_USER_DATA");

        token = getArguments().getString("TOKEN");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        User user = request.getUser();

        View rootView = inflater.inflate(R.layout.request_pal_detail, container, false);
        NameView = (TextView) rootView.findViewById(R.id.NameText);
        BoozeListView = (ListView) rootView.findViewById(R.id.mypal_boozelist);
        PubListView = (ListView) rootView.findViewById(R.id.mypal_publist);
        dateView = (TextView) rootView.findViewById(R.id.request_date_text);
        pubView = (TextView) rootView.findViewById(R.id.request_pub_text);
        ratingBar = (RatingBar) rootView.findViewById(R.id.request_price);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RespondRequestTask rt = new RespondRequestTask(RequestDetailFragment.this.getContext(),
                        RequestDetailFragment.this.loggedUser.getId(),
                        RequestDetailFragment.this.request.getUser().getId(),
                        true);
                rt.delegate = RequestDetailFragment.this;
                rt.execute();
            }
        });

        denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RespondRequestTask rt = new RespondRequestTask(RequestDetailFragment.this.getContext(),
                        RequestDetailFragment.this.loggedUser.getId(),
                        RequestDetailFragment.this.request.getUser().getId(),
                        false);
                rt.delegate = RequestDetailFragment.this;
                rt.execute();
            }
        });


        NameView.setText(this.request.getUser().getUsername());

        if(user.getFavouriteDrinks().isEmpty()){
            final ArrayAdapter BoozeAdapter = new ArrayAdapter(getActivity(),
                    android.R.layout.simple_list_item_1, Arrays.asList("Opps - user has no favourite drink :("));
            BoozeListView.setAdapter(BoozeAdapter);
        }else{
            final ArrayAdapter BoozeAdapter = new ArrayAdapter(getActivity(),
                    android.R.layout.simple_list_item_1, user.getFavouriteDrinks());
            BoozeListView.setAdapter(BoozeAdapter);
        }

        if(user.getFavouritePub().isEmpty()){
            final ArrayAdapter PubAdapter = new ArrayAdapter(getActivity(),
                    android.R.layout.simple_list_item_1, Arrays.asList("Oops - user has no favourite pub :("));
            PubListView.setAdapter(PubAdapter);
        }else{
            final ArrayAdapter PubAdapter = new ArrayAdapter(getActivity(),
                    android.R.layout.simple_list_item_1, user.getFavouritePub());
            PubListView.setAdapter(PubAdapter);
        }

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

        SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd - hh:mm");
        Date date = request.getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        dateView.setText(format.format(calendar.getTime()));
        if (request.getPub() != null)
            pubView.setText(request.getPub().toString());


        ratingBar.setRating(request.getUser().getPriceCategory());

        return rootView;
    }

    @Override
    public void onTaskFinished(Integer result) {
        GetUserTask dt = new GetUserTask(getContext());
        dt.delegate = this;
        dt.execute(this.token);
    }

    @Override
    public void onTaskFinished(User user, String tag) {
        if (tag.equals("GETUSER")) {
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra("USER_DATA", user);
            intent.putExtra("TOKEN", token);
            startActivity(intent);
        }
    }
}
