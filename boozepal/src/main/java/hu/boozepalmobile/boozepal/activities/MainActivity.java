package hu.boozepalmobile.boozepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;

import hu.boozepalmobile.boozepal.fragments.MyPalFragment;
import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.fragments.RequestFragment;
import hu.boozepalmobile.boozepal.fragments.UserFragment;
import hu.boozepalmobile.boozepal.models.PalRequest;
import hu.boozepalmobile.boozepal.models.Token;
import hu.boozepalmobile.boozepal.models.User;

public class MainActivity extends AppCompatActivity
        implements UserFragment.OnListFragmentInteractionListener, MyPalFragment.OnListFragmentInteractionListener, RequestFragment.OnListFragmentInteractionListener {

    private final String TAG = "MainActivity";

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private User user;
    private String token;

    private ArrayList<User> myPals;
    private ArrayList<User> currentPals;
    private ArrayList<User> requestPals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            user = b.getParcelable("USER_DATA");
            token = b.getString("TOKEN");
        }

        System.out.println(user.toString());

        Token.setToken(token);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("CURRENT_PALS", currentPals);
        bundle.putParcelableArrayList("MY_PALS", myPals);
        bundle.putParcelableArrayList("PALS", requestPals);
        bundle.putParcelable("USER_DATA", user);
        bundle.putString("TOKEN", token);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), bundle);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        setupView();
    }

    void setupView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(user.getUsername());

        ImageButton calendarButton = (ImageButton) toolbar.findViewById(R.id.calendar_button);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PalListActivity", "Clicked on calendar button");

                Intent intent = new Intent(v.getContext(), CalendarActivity.class);
                intent.putExtra("USER_DATA", user);
                intent.putExtra("TOKEN", token);
                startActivity(intent);
            }
        });

        ImageButton settingsButton = (ImageButton) toolbar.findViewById(R.id.setting_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PalListActivity", "Clicked on settings button");

                Intent intent = new Intent(v.getContext(), SettingsActivity.class);
                intent.putExtra("USER_DATA", user);
                intent.putExtra("TOKEN", token);
                startActivity(intent);
            }
        });

        ImageButton logoutButton = (ImageButton) toolbar.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PalListActivity", "Clicked on logout button!");

                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void logOut() {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(User item) {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private Bundle fragmentBundle;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public SectionsPagerAdapter(FragmentManager fm, Bundle data) {
            super(fm);
            fragmentBundle = data;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    UserFragment userFragment = new UserFragment();
                    Bundle bundle = new Bundle();

                    bundle.putParcelableArrayList("CURRENT_PALS", fragmentBundle.getParcelableArrayList("CURRENT_PALS"));
                    bundle.putParcelable("USER_DATA", fragmentBundle.getParcelable("USER_DATA"));
                    bundle.putString("TOKEN", fragmentBundle.getString("TOKEN"));
                    userFragment.setArguments(bundle);
                    return userFragment;
                case 1:
                    RequestFragment requestFragment = new RequestFragment();
                    Bundle bundle_request = new Bundle();
                    bundle_request.putParcelableArrayList("PALS", fragmentBundle.getParcelableArrayList("PALS"));
                    bundle_request.putParcelable("USER_DATA", fragmentBundle.getParcelable("USER_DATA"));
                    bundle_request.putString("TOKEN", fragmentBundle.getString("TOKEN"));
                    requestFragment.setArguments(bundle_request);
                    return requestFragment;
                case 2:
                    MyPalFragment myPalFragment = new MyPalFragment();
                    Bundle bundle_mypal = new Bundle();
                    bundle_mypal.putParcelableArrayList("MY_PALS", fragmentBundle.getParcelableArrayList("MY_PALS"));
                    bundle_mypal.putParcelable("USER_DATA", fragmentBundle.getParcelable("USER_DATA"));
                    bundle_mypal.putString("TOKEN", fragmentBundle.getString("TOKEN"));
                    myPalFragment.setArguments(bundle_mypal);
                    return myPalFragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "BoozePals";
                case 1:
                    return "Request";
                case 2:
                    return "MyPals";
            }
            return null;
        }
    }
}
