package hu.boozepalmobile.boozepal.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.fragments.RequestDetailFragment;
import hu.boozepalmobile.boozepal.fragments.RequestFragment.OnListFragmentInteractionListener;
import hu.boozepalmobile.boozepal.models.User;

import java.util.List;

public class MyRequestRecyclerViewAdapter extends RecyclerView.Adapter<MyRequestRecyclerViewAdapter.ViewHolder> {
    private User user;
    private String token;
    private final List<User> users;
    private final OnListFragmentInteractionListener mListener;

    public MyRequestRecyclerViewAdapter(List<User> items, OnListFragmentInteractionListener listener, User user, String token) {
        users = items;
        mListener = listener;
        this.user = user;
        this.token = token;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = users.get(position);
        //holder.mIdView.setText(mValues.get(position).id);
        //holder.mContentView.setText(mValues.get(position).content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                   // mListener.onListFragmentInteraction(holder.user);

                    Intent intent = new Intent(v.getContext(), RequestDetailFragment.class);
                    intent.putExtra("LOGGED_USER_DATA", user);
                    intent.putExtra("USER_DATA", users.get(position));
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
        public final TextView DateView;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            NameView = (TextView) view.findViewById(R.id.fragment_request_name);
            CityView = (TextView) view.findViewById(R.id.fragment_request_city);
            DateView = (TextView) view.findViewById(R.id.fragment_request_date);
            //mIdView = (TextView) view.findViewById(R.id.id);
            //mContentView = (TextView) view.findViewById(R.id.content);
        }

    }
}
