package hu.boozepalmobile.boozepal.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import hu.boozepalmobile.boozepal.activities.PalDetailActivity;
import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.models.User;
import hu.boozepalmobile.boozepal.fragments.UserFragment.OnListFragmentInteractionListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link User} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CurrentPalsRecyclerViewAdapter extends RecyclerView.Adapter<CurrentPalsRecyclerViewAdapter.ViewHolder> {

    private User user;
    private String token;
    private final List<User> mValues;
    private final OnListFragmentInteractionListener mListener;

    public CurrentPalsRecyclerViewAdapter(List<User> items, OnListFragmentInteractionListener listener, User user, String token) {
        mValues = items;
        mListener = listener;
        this.user = user;
        this.token = token;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_pals, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.user = mValues.get(position);
        holder.nameView.setText(mValues.get(position).getName());
        holder.cityView.setText(mValues.get(position).getCity());
        //holder.mContentView.setText(mValues.get(position).content);

        final int f_position = position;

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.user);

                    Intent intent = new Intent(v.getContext(), PalDetailActivity.class);
                    intent.putExtra("SELECTED_USER_DATA", mValues.get(f_position));
                    intent.putExtra("USER_DATA", user);
                    intent.putExtra("TOKEN", token);
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nameView;
        public final TextView cityView;
        public User user;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameView = (TextView) view.findViewById(R.id.fragment_pals_name);
            cityView = (TextView) view.findViewById(R.id.fragment_pals_city);
           // System.out.println(user.getName());
           // nameView.setText(user.getName());
        }


    }
}
