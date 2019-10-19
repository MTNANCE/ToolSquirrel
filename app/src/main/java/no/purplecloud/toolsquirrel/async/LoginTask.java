package no.purplecloud.toolsquirrel.async;

import android.os.AsyncTask;

import com.mashape.unirest.http.exceptions.UnirestException;

import no.purplecloud.toolsquirrel.APIClient;
import no.purplecloud.toolsquirrel.listener.LoginListener;

public class LoginTask extends AsyncTask<String, String, String> {
    private String username;
    private String password;

    private LoginListener listener;

    public LoginTask(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginTask setCallback(LoginListener listener) {
        this.listener = listener;
        return this;
    }


    private void notifyListeners(boolean success, String msg) {
        if (listener != null) {
            listener.onLogin(success, msg);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        boolean isLoggedIn = false;
        String msg = "";

        try {
            isLoggedIn = APIClient.getInstance().login(username, password);

            if (isLoggedIn) {
                msg = "Logged in!";
            } else {
                msg = "Failed to login";
            }

        } catch (UnirestException e) {
            e.printStackTrace();
            msg = "Internal error on login";

        } finally {
            notifyListeners(isLoggedIn, msg);
        }

        // We do not need to return anything
        return null;
    }
}
