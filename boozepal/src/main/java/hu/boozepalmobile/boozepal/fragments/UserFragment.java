package hu.boozepalmobile.boozepal.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.adapters.CurrentPalsRecyclerViewAdapter;
import hu.boozepalmobile.boozepal.models.User;
import hu.boozepalmobile.boozepal.network.FindPalsResponse;
import hu.boozepalmobile.boozepal.network.FindPalsTask;

import java.util.ArrayList;

public class UserFragment extends Fragment implements FindPalsResponse{
    private OnListFragmentInteractionListener mListener;
    private ArrayList<User> userList;
    private User user;
    private String token;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rv;
    private CurrentPalsRecyclerViewAdapter adapter;

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

        rv = (RecyclerView) view.findViewById(R.id.currentpal_list);
        refreshItems();
        //rv.setAdapter(new CurrentPalsRecyclerViewAdapter(userList, mListener,user, token));

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.current_pal_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                System.out.println("juppi");
                refreshItems();
            }
        });
        // Set the adapter
       /* if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setAdapter(new CurrentPalsRecyclerViewAdapter(userList, mListener,user, token));
        }*/

        return view;
    }


    void refreshItems(){
        FindPalsTask fpTask = new FindPalsTask(getContext(), this.token);
        fpTask.delegate = this;
        fpTask.execute(this.user);
        //onItemsLoaded();
    }

    void onItemsLoaded(){
        swipeRefreshLayout.setRefreshing(false);
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

    @Override
    public void onTaskFinished(ArrayList<User> result) {
        userList = result;
        rv.setAdapter(new CurrentPalsRecyclerViewAdapter(userList, mListener,user, token));
        rv.invalidate();
        swipeRefreshLayout.setRefreshing(false);
        System.out.println("fickyeah");
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(User item);
    }


}
