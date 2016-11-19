package hu.boozepalmobile.boozepal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import hu.boozepalmobile.boozepal.User.User;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private String token;

    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions googleSignInOptions;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.sign_in_button).setOnClickListener(this);

        configureSignIn();

        checkNetworkConnection();
    }

    private void configureSignIn() {
        Log.d("LoginActivity","Configure sign-in");
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this).
                enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

    }

    @Override
    protected void onStart() {
        super.onStart();

        /*OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);

        if (opr.isDone()) {
            System.out.println("Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }*/

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9001) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, 9001);
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            System.out.println(account.getDisplayName() + " " + account.getIdToken() + account.getEmail());

            this.token = account.getIdToken();

            LoginTask ltask = new LoginTask();
            ltask.execute(account.getIdToken());
        } else {
            System.out.println("Something went wrong");
        }

    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setIndeterminate(true);
        }

        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    private void checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            System.out.println("Network is OK");
        } else {
            System.out.println("Could not connect to network!");
        }

    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }


    private class LoginTask extends AsyncTask<String, Void, String> {

        private String token;

        @Override
        protected String doInBackground(String... params) {
            token = params[0];
            String result = authenticate(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println(s);

            try {
                JSONObject obj = new JSONObject(s);

                if(obj.length() == 0){
                    Log.v(TAG, "Error occured during login!");
                    return;
                }

                List<String> favouriteBoozes = new ArrayList<>();
                if(!obj.isNull("favouriteDrink")) {
                    JSONArray boozes = obj.getJSONArray("favouriteDrink");
                    for (int i = 0; i < boozes.length(); ++i) {
                        JSONObject booze = boozes.getJSONObject(i);
                        Iterator<?> keys = booze.keys();

                        while (keys.hasNext()) {
                            String key = (String) keys.next();
                            //favouriteBoozes.add((String) booze.get(key));
                            favouriteBoozes.add(key);
                        }
                    }
                }

                List<String> favouritePubs = new ArrayList<>();
                if(!obj.isNull("favouritePub")) {
                    JSONArray pubs = obj.getJSONArray("favouritePub");
                    for (int i = 0; i < pubs.length(); ++i) {
                        JSONObject pub = pubs.getJSONObject(i);
                        Iterator<?> keys = pub.keys();

                        while (keys.hasNext()) {
                            String key = (String) keys.next();
                            //favouritePubs.add((String) booze.get(key));
                            favouritePubs.add(key);
                        }
                    }
                }

                List<Date> savedDates = new ArrayList<>();
                if(!obj.isNull("savedDates")) {
                    JSONArray dates = obj.getJSONArray("savedDates");
                    for (int i = 0; i < dates.length(); ++i) {
                        JSONObject date = dates.getJSONObject(i);
                        Iterator<?> keys = date.keys();

                        while (keys.hasNext()) {
                            DateFormat format = new SimpleDateFormat("MMMM d yyyy", Locale.ENGLISH);
                            Date key = format.parse(keys.next().toString());
                            //favouritePubs.add((String) booze.get(key));
                            savedDates.add(key);
                        }
                    }
                }


                String name = "";
                if(!obj.isNull("fullName"))
                    name = obj.getString("fullName");

                String city = "";
                if(!obj.isNull("city"))
                    city = obj.getString("city");

                String id = "";
                if(!obj.isNull("id"))
                    id = obj.getString("id");

                int radius = 10;
                if(!obj.isNull("searchRadius"))
                    radius = Integer.parseInt(obj.getString("searchRadius"));

                int priceCategory = 1;
                if(!obj.isNull("priceCategory"))
                    priceCategory = Integer.parseInt(obj.getString("priceCategory"));

                User user = new User(id, name, city, favouriteBoozes, favouritePubs, radius, priceCategory, savedDates);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("USER_DATA", user);
                intent.putExtra("TOKEN", LoginActivity.this.token);
                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //super.onPostExecute(s);
        }

        private String authenticate(String token) {
            URL url = null;
            InputStream is = null;
            String result = null;
            try {
                url = new URL(getString(R.string.rest_url_login));

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "*/*");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.connect();

                JSONObject obj = new JSONObject();
                obj.put("token", token);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(obj.toString());
                writer.close();
                os.close();

                //Read
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                String line = null;
                StringBuilder sb = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                br.close();
                result = sb.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return result;

        }
    }

}
