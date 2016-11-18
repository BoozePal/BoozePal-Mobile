package hu.boozepalmobile.boozepal;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hu.boozepalmobile.boozepal.User.User;
import hu.boozepalmobile.boozepal.UserFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link User} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CurrentPalsRecyclerViewAdapter extends RecyclerView.Adapter<CurrentPalsRecyclerViewAdapter.ViewHolder> {

    private final List<User> mValues;
    private final OnListFragmentInteractionListener mListener;

    public CurrentPalsRecyclerViewAdapter(List<User> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.user = mValues.get(position);
        holder.nameView.setText(mValues.get(position).getName());
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
                    intent.putExtra("USER_DATA", mValues.get(f_position));
                    //intent.putExtra("TOKEN", .this.token);
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
        public User user;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameView = (TextView) view.findViewById(R.id.currentpal_list_item);
           // System.out.println(user.getName());
           // nameView.setText(user.getName());
        }


    }
}
