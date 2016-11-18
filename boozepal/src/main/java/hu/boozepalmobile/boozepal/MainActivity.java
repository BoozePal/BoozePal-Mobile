package hu.boozepalmobile.boozepal;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hu.boozepalmobile.boozepal.User.User;

public class MainActivity extends AppCompatActivity
        implements UserFragment.OnListFragmentInteractionListener, MyPalFragment.OnListFragmentInteractionListener{

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private User user;
    private String token;

    private ArrayList<User> myPals;
    private ArrayList<User> currentPals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myPals = new ArrayList<>();
        User user_ = new User("2", "Jonas", "Debrecen", new ArrayList<String>(), new ArrayList<String>(), 30, 2, new ArrayList<Date>());
        User user_2 = new User("3", "Jonaska", "Karcag", new ArrayList<String>(), new ArrayList<String>(), 30, 2, new ArrayList<Date>());
        currentPals = new ArrayList<>();
        currentPals.add(user_);
        currentPals.add(user_2);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("CURRENT_PALS", currentPals);
        bundle.putParcelableArrayList("MY_PALS", myPals);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), bundle);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            user = b.getParcelable("USER_DATA");
            token = b.getString("TOKEN");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        public SectionsPagerAdapter(FragmentManager fm, Bundle data)
        {
            super(fm);
            fragmentBundle = data;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    UserFragment userFragment = new UserFragment();
                    Bundle bundle = new Bundle();

                    bundle.putParcelableArrayList("CURRENT_PALS",fragmentBundle.getParcelableArrayList("CURRENT_PALS"));
                    userFragment.setArguments(bundle);
                    return userFragment;
                case 1:
                    MyPalFragment myPalFragment = new MyPalFragment();
                    Bundle bundle_mypal = new Bundle();
                    bundle_mypal.putParcelableArrayList("MY_PALS",fragmentBundle.getParcelableArrayList("MY_PALS"));
                    myPalFragment.setArguments(bundle_mypal);
                    return myPalFragment;
            }

            return null;
            //return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "BoozePals";
                case 1:
                    return "MyPals";
            }
            return null;
        }
    }
}
