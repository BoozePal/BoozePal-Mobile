package hu.boozepalmobile.boozepal.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.activities.MyPalDetailActivity;
import hu.boozepalmobile.boozepal.fragments.MyPalFragment.OnListFragmentInteractionListener;
import hu.boozepalmobile.boozepal.models.User;
import hu.boozepalmobile.boozepal.utils.UIPalRequest;

import java.util.List;

public class MyPalsRecyclerViewAdapter extends RecyclerView.Adapter<MyPalsRecyclerViewAdapter.ViewHolder> {

    private final String TAG = "MyPalsRecyclerVA";

    private User user;
    private String token;
    private final List<UIPalRequest> users;
    private final OnListFragmentInteractionListener mListener;

    public MyPalsRecyclerViewAdapter(List<UIPalRequest> items, OnListFragmentInteractionListener listener, User user, String token) {
        users = items;
        mListener = listener;
        this.user = user;
        this.token = token;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_mypals, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = users.get(position);
        if(users.get(position).getUser() != null) {
            holder.NameView.setText(users.get(position).getUser().getUsername());
            if(users.get(position).getUser().getAddress() != null)
                holder.CityView.setText(users.get(position).getUser().getAddress().getTown());
        }

        final int f_position = position;
        Log.d(TAG, String.valueOf(f_position));
        Log.d(TAG, users.get(f_position).toString());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    Intent intent = new Intent(v.getContext(), MyPalDetailActivity
                            .class);
                    intent.putExtra("USER_DATA", user);
                    intent.putExtra("SELECTED_REQUEST_DATA", users.get(f_position));
                    intent.putExtra("TOKEN", token);
                    v.getContext().startActivity(intent);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView NameView;
        public final TextView CityView;
        public UIPalRequest mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            NameView = (TextView) view.findViewById(R.id.fragment_mypals_name);
            CityView = (TextView) view.findViewById(R.id.fragment_mypals_city);
        }

    }
}
