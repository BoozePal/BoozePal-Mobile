package hu.boozepalmobile.boozepal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.fragments.RequestDetailFragment;
import hu.boozepalmobile.boozepal.models.PalRequest;
import hu.boozepalmobile.boozepal.models.User;
import hu.boozepalmobile.boozepal.utils.UIPalRequest;

/**
 * Created by fanny on 2016.12.03..
 */

public class RequestDetailActivity extends AppCompatActivity {

    private final String TAG = "RequestDetailActivity";

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

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle b = getIntent().getExtras();
        if(b != null) {
            System.out.println("what");
            loggedUser = b.getParcelable("LOGGED_USER_DATA");
            request = b.getParcelable("SELECTED_REQUEST_DATA");
            user = b.getParcelable("SELECTED_USER_DATA");
            token = b.getString("TOKEN");
            type = b.getInt("TYPE");
        }

        if (savedInstanceState == null) {
                Bundle arguments = new Bundle();
                arguments.putParcelable("SELECTED_REQUEST_DATA", request);
                arguments.putParcelable("LOGGED_USER_DATA", loggedUser);
                arguments.putString("TOKEN", token);
                RequestDetailFragment fragment = new RequestDetailFragment();
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
