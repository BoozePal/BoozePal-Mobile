package hu.boozepalmobile.boozepal.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import hu.boozepalmobile.boozepal.R;
import hu.boozepalmobile.boozepal.models.Token;
import hu.boozepalmobile.boozepal.network.logout.LogoutTask;

/**
 * Created by fanny on 2016.11.26..
 */

public class BoozePalApplication extends Application implements Application.ActivityLifecycleCallbacks{
    private int resumed;
    private int paused;
    private int started;
    private int stopped;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resumed;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++paused;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
        if(stopped == started){
            Log.d("BoozePalApplication","Application is in the background");
            if(Token.getToken() != null){
                Log.d("BoozePalApplication","Remote logout started");
                LogoutTask lTask = new LogoutTask();
                lTask.execute(Token.getToken(), getString(R.string.rest_url_logout));
            }
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
