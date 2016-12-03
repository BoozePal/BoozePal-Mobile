package hu.boozepalmobile.boozepal.network.updateuser;

import android.content.Context;
import android.os.AsyncTask;

import hu.boozepalmobile.boozepal.models.User;

/**
 * Created by fanny on 2016.11.28..
 */

public class UpdateUserTask extends AsyncTask<User,Void,Integer>{

    private Context context;
    private String token;

    public UpdateUserTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
    }

    @Override
    protected Integer doInBackground(User... params) {
        return 0;
    }

    private String saveSettings(User user) {
        return null;
    }
}
