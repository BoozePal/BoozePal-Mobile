package hu.boozepalmobile.boozepal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import hu.boozepalmobile.boozepal.User.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PalListActivity extends AppCompatActivity {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pal_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.palRefreshButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "This button will refresh", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ImageButton settingsButton = (ImageButton) toolbar.findViewById(R.id.settingButton);
        settingsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SettingsMainActivity.class);
                startActivity(intent);
            }
        });

        View recyclerView = findViewById(R.id.pal_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.pal_detail_container) != null) {
            mTwoPane = true;
        }

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        List<String> l1 = Arrays.asList("vodka", "rum", "bor", "heineken");
        List<String> l2 = Arrays.asList("vodka", "rum", "konyak", "heineken");
        List<String> l3 = Arrays.asList("Ibolya", "Pikolo", "Valhalla");
        List<String> l4 = Arrays.asList("Ibolya", "Valhalla");
        User us1 = new User("1","Jonas", "Male", l1, l3);
        User us2 = new User("2","Bodza", "Female", l2, l4);

        List<User> users = new ArrayList<>();
        users.add(us1);
        users.add(us2);

        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(users));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<User> Users;

        public SimpleItemRecyclerViewAdapter(List<User> items) {
            Users = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.pal_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.userData = Users.get(position);
            holder.mIdView.setText(Users.get(position).getId());
            holder.mNameView.setText(Users.get(position).getName());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        System.out.println("ha");
                        Bundle arguments = new Bundle();
                        arguments.putString(PalDetailFragment.ARG_ITEM_ID, holder.userData.getId());
                        arguments.putParcelable("USER_DATA", holder.userData);
                        PalDetailFragment fragment = new PalDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.pal_detail_container, fragment)
                                .commit();
                    } else {
                        System.out.println("haha");
                        Context context = v.getContext();
                        Intent intent = new Intent(context, PalDetailActivity.class);
                        intent.putExtra(PalDetailFragment.ARG_ITEM_ID, holder.userData.getId());
                        intent.putExtra("USER_DATA", holder.userData);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return Users.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mNameView;
            public User userData;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mNameView = (TextView) view.findViewById(R.id.nameList);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mNameView.getText() + "'";
            }
        }
    }

}
