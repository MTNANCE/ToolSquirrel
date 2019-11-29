package no.purplecloud.toolsquirrel;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.auth0.android.jwt.DecodeException;
import com.auth0.android.jwt.JWT;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import no.purplecloud.toolsquirrel.domain.Employee;
import no.purplecloud.toolsquirrel.listener.LoginListener;
import no.purplecloud.toolsquirrel.network.VolleySingleton;
import no.purplecloud.toolsquirrel.singleton.CacheSingleton;

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
    private Employee client;

    public void login(String username, String password, Context context, LoginListener l) {
        try {
            JSONObject credentials = new JSONObject() {{
                put("username", username);
                put("password", password);
            }};

            StringRequest request = new StringRequest(Request.Method.POST, Endpoints.URL + "/login",
                    null, e -> l.onLogin(false, "Format or credentials is incorrect")) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return new HashMap<String, String>() {{
                        put("Content-Type", "application/json");
                        put("Accept", "application/json");
                    }};
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return credentials.toString().getBytes();
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    CacheSingleton.getInstance(context).saveToCache("token",
                            response.headers.get("Authorization").split(" ")[1]);
                    l.onLogin(true, "Logged in!");
                    return super.parseNetworkResponse(response);
                }
            };

            VolleySingleton.getInstance(context).addToRequestQueue(request);
        } catch (Exception e) {
            l.onLogin(false, "Failed to login");
            e.printStackTrace();
        }
    }
}
