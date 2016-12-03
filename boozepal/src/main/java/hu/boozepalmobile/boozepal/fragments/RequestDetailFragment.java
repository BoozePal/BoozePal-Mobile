package hu.boozepalmobile.boozepal.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.activities.MainActivity;
import hu.boozepalmobile.boozepal.models.User;
import hu.boozepalmobile.boozepal.network.getpal.GetPalResponse;
import hu.boozepalmobile.boozepal.network.getuser.GetUserResponse;
import hu.boozepalmobile.boozepal.network.getuser.GetUserTask;
import hu.boozepalmobile.boozepal.network.respontrequest.RespondRequestResponse;
import hu.boozepalmobile.boozepal.network.respontrequest.RespondRequestTask;

public class RequestDetailFragment extends Fragment implements RespondRequestResponse, GetUserResponse{
    private User userData;
    private String token;
    private User loggedUser;

    public TextView NameView;
    public ListView BoozeListView;
    public ListView PubListView;

    private Button acceptButton;
    private Button denyButton;

    public RequestDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_collapsing_requestdetail);

        if (getArguments().containsKey("SELECTED_REQUEST_DATA")) {
            userData = getArguments().getParcelable("SELECTED_REQUEST_DATA");
            if (appBarLayout != null) {
                appBarLayout.setTitle(userData.getUsername());
            }
            loggedUser = getArguments().getParcelable("LOGGED_USER_DATA");

            token = getArguments().getString("TOKEN");
        }

        System.out.println(userData.getId() + " " + loggedUser.getId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.request_pal_detail, container, false);
        NameView = (TextView) rootView.findViewById(R.id.NameText);
        BoozeListView = (ListView) rootView.findViewById(R.id.BoozeListView);
        PubListView = (ListView) rootView.findViewById(R.id.PubListView);

        acceptButton = (Button) rootView.findViewById(R.id.accept_button);
        denyButton = (Button) rootView.findViewById(R.id.deny_button);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RespondRequestTask rt = new RespondRequestTask(RequestDetailFragment.this.getContext(),
                                                                RequestDetailFragment.this.loggedUser.getId(),
                                                                RequestDetailFragment.this.userData.getId(),
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
                        RequestDetailFragment.this.userData.getId(),
                        false);
                rt.delegate = RequestDetailFragment.this;
                rt.execute();
            }
        });

        if (userData != null) {
            NameView.setText(userData.getUsername());

            final ArrayAdapter BoozeAdapter = new ArrayAdapter(getActivity(),
                    android.R.layout.simple_list_item_1, userData.getFavouriteDrinks());
            BoozeListView.setAdapter(BoozeAdapter);
            final ArrayAdapter PubAdapter = new ArrayAdapter(getActivity(),
                    android.R.layout.simple_list_item_1, userData.getFavouritePub());
            PubListView.setAdapter(PubAdapter);
        }

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
        if(tag.equals("GETUSER")){
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra("USER_DATA", user);
            intent.putExtra("TOKEN", token);
            startActivity(intent);
        }
    }
}
