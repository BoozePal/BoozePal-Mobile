package hu.boozepalmobile.boozepal.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hu.boozepalmobile.boozepal.adapters.MyPalsRecyclerViewAdapter;
import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.models.PalRequest;
import hu.boozepalmobile.boozepal.models.User;
import hu.boozepalmobile.boozepal.network.getpal.GetPalResponse;
import hu.boozepalmobile.boozepal.network.getpal.GetPalTask;
import hu.boozepalmobile.boozepal.network.getuser.GetUserResponse;
import hu.boozepalmobile.boozepal.network.getuser.GetUserTask;
import hu.boozepalmobile.boozepal.utils.UIPalRequest;

public class MyPalFragment extends Fragment implements GetUserResponse, GetPalResponse {

    private final String TAG = "MyPalFragment";

    private OnListFragmentInteractionListener mListener;
    private ArrayList<UIPalRequest> requestList;
    private User user;
    private String token;

    private View view;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rv;

    public MyPalFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestList = new ArrayList<>();

        if (getArguments() != null) {
            this.user = getArguments().getParcelable("USER_DATA");
            this.token = getArguments().getString("TOKEN");
            System.out.println(this.user.getUsername());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mypal_list, container, false);

        rv = (RecyclerView) view.findViewById(R.id.mypal_list);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.mypal_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                System.out.println("juppi");
                refreshItems();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for (PalRequest pal : this.user.getActualPals().values()) {
            Log.d(TAG, pal.getRequesterUserId().toString());
            if (pal.isAccepted()) {
                GetPalTask gt = new GetPalTask(getContext());
                gt.delegate = this;
                gt.execute(pal.getRequesterUserId());
            }
        }
    }

    void refreshItems() {
        GetUserTask gTask = new GetUserTask(getContext());
        gTask.delegate = this;
        gTask.execute(this.token);
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
    public void onTaskFinished(User user, String tag) {
        if (tag.equals("GETUSER")) {
            this.requestList = new ArrayList<>();
            for (PalRequest pal : this.user.getActualPals().values()) {
                Log.d(TAG, pal.getRequesterUserId().toString());
                if (pal.isAccepted()) {
                    GetPalTask gt = new GetPalTask(getContext());
                    gt.delegate = this;
                    gt.execute(pal.getRequesterUserId());
                }
            }
            rv.setAdapter(new MyPalsRecyclerViewAdapter(requestList, mListener, user, token));
            swipeRefreshLayout.setRefreshing(false);
        } else if (tag.equals("GETPAL")) {
            for (Long l : this.user.getActualPals().keySet()) {
                if (this.user.getActualPals().get(l).getRequesterUserId().equals(user.getId())) {
                    PalRequest pr = this.user.getActualPals().get(l);
                    UIPalRequest p = new UIPalRequest(user, pr.getPub(), pr.getDate(), pr.isAccepted());
                    this.requestList.add(p);
                    break;
                }
            }
            Log.d(TAG, requestList.toString());
            rv.setAdapter(new MyPalsRecyclerViewAdapter(requestList, mListener, user, token));
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(User item);
    }
}
