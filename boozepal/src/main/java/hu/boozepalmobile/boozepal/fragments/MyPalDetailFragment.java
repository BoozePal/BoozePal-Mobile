package hu.boozepalmobile.boozepal.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.models.User;


public class MyPalDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";

    private User UserData;
    private User loggedUser;

    public TextView NameView;
    public TextView GenderView;
    public ListView BoozeListView;
    public ListView PubListView;
    public MaterialCalendarView calendarView;

    public MyPalDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);

        if(getArguments().containsKey("USER_DATA")){
            UserData = (User) getArguments().getParcelable("USER_DATA");
            if (appBarLayout != null) {
                appBarLayout.setTitle(UserData.getName());
            }
            loggedUser = (User) getArguments().getParcelable("LOGGED_USER_DATA");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pal_detail, container, false);
        //NameView = (TextView) rootView.findViewById(R.id.NameText);
        //GenderView = (TextView) rootView.findViewById(R.id.GenderText);
        //BoozeListView = (ListView) rootView.findViewById(R.id.BoozeListView);
        //PubListView = (ListView) rootView.findViewById(R.id.PubListView);

        if (UserData != null) {
            /*NameView.setText(UserData.getName());

            final ArrayAdapter BoozeAdapter = new ArrayAdapter(getActivity(),
                    android.R.layout.simple_list_item_1, UserData.getBoozes());
            BoozeListView.setAdapter(BoozeAdapter);
            final ArrayAdapter PubAdapter = new ArrayAdapter(getActivity(),
                    android.R.layout.simple_list_item_1, UserData.getPubs());
            PubListView.setAdapter(PubAdapter);*/
        }

        return rootView;
    }
}
