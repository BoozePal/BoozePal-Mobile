package hu.boozepalmobile.boozepal.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.adapters.CurrentPalsRecyclerViewAdapter;
import hu.boozepalmobile.boozepal.models.User;

import java.util.ArrayList;

public class UserFragment extends Fragment {
    private OnListFragmentInteractionListener mListener;
    private ArrayList<User> userList;
    private User user;
    private String token;

    public UserFragment() {
    }

    /*public static UserFragment newInstance(List<User> userList) {
        UserFragment fragment = new UserFragment();
        this.userList = userList;
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userList = new ArrayList<>();

        if (getArguments() != null) {

            this.userList = getArguments().getParcelableArrayList("CURRENT_PALS");
            this.user = getArguments().getParcelable("USER_DATA");
            this.token = getArguments().getString("TOKEN");
            System.out.println(this.user.getName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pals_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setAdapter(new CurrentPalsRecyclerViewAdapter(userList, mListener,user, token));
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(User item);
    }
}