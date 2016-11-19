package hu.boozepalmobile.boozepal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import hu.boozepalmobile.boozepal.User.User;

public class PalDetailActivity extends AppCompatActivity {

    private User loggedUser;
    private User user;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pal_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle b = getIntent().getExtras();
        if(b != null) {
            System.out.println("what");
            user = b.getParcelable("USER_DATA");
            token = b.getString("TOKEN");
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(PalDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(PalDetailFragment.ARG_ITEM_ID));
            arguments.putParcelable("USER_DATA", getIntent().getParcelableExtra("SELECTED_USER_DATA"));
            arguments.putParcelable("LOGGED_USER_DATA", getIntent().getParcelableExtra("USER_DATA"));
            arguments.putString("TOKEN", getIntent().getStringExtra("TOKEN"));
            PalDetailFragment fragment = new PalDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.pal_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            System.out.println("hehe");
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("USER_DATA", this.user);
            intent.putExtra("TOKEN", this.token);
            startActivity(intent);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
