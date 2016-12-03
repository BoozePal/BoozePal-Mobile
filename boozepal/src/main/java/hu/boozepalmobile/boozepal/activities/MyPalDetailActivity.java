package hu.boozepalmobile.boozepal.activities;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.fragments.MyPalDetailFragment;
import hu.boozepalmobile.boozepal.fragments.RequestDetailFragment;
import hu.boozepalmobile.boozepal.models.PalRequest;
import hu.boozepalmobile.boozepal.models.User;
import hu.boozepalmobile.boozepal.utils.UIPalRequest;

public class MyPalDetailActivity extends AppCompatActivity {
    private User loggedUser;
    private User user;
    private String token;
    private UIPalRequest request;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_request_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle b = getIntent().getExtras();
        if(b != null) {
            loggedUser = b.getParcelable("USER_DATA");
            request = b.getParcelable("SELECTED_REQUEST_DATA");
            user = b.getParcelable("SELECTED_USER_DATA");
            token = b.getString("TOKEN");
            type = b.getInt("TYPE");
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable("SELECTED_USER_DATA", request.getRequesterUser());
            arguments.putParcelable("USER_DATA", getIntent().getParcelableExtra("USER_DATA"));
            arguments.putString("TOKEN", getIntent().getStringExtra("TOKEN"));
            MyPalDetailFragment fragment = new MyPalDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.request_detail_container, fragment)
                    .commit();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("USER_DATA", this.loggedUser);
            intent.putExtra("TOKEN", this.token);
            startActivity(intent);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
