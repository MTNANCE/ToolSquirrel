package no.purplecloud.toolsquirrel;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class APIClient {
    // Instance
    private static APIClient instance;
    public static APIClient getInstance() {
        if (instance == null) {
            instance = new APIClient();
        }

        return instance;
    }

    private String token;

    public boolean login(String username, String password) throws UnirestException {
        boolean loggedIn = false;

        HttpResponse<String> response = Unirest
                .post(Endpoints.LOGIN)
                .field("username", username)
                .field("password", password)
                .asString();

        if (response.getCode() == 200) {
            // Assume token is returned as response on '200 OK'
            token = response.getBody();
            loggedIn = true;
        }

        return loggedIn;
    }

    public boolean isLoggedIn() {
        boolean loggedIn = false;

        if (token != null) {
            //TODO Check token expiration date
            loggedIn = true;
        }

        return loggedIn;
    }
}
