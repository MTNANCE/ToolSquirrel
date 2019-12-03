package no.purplecloud.toolsquirrel.ui.credentials;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import no.purplecloud.toolsquirrel.Endpoints;
import no.purplecloud.toolsquirrel.MainActivity;
import no.purplecloud.toolsquirrel.R;
import no.purplecloud.toolsquirrel.listener.LoginListener;
import no.purplecloud.toolsquirrel.network.VolleySingleton;
import no.purplecloud.toolsquirrel.singleton.CacheSingleton;

public class LoginFragment extends Fragment implements LoginListener {

    private TextView txtUsername;
    private TextView txtPassword;
    private TextView txtRegister;
    private Button btnLogin;

    private RegisterFragment regFragment;

    @Override
    public void onStart() {
        super.onStart();

        // We use this function so it doesn't create the view before checking if client already
        // is logged in. This prevents the "show view" animation then it switches to the main one if
        // it's not done like this

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Get elements from the view
        txtUsername = view.findViewById(R.id.txtUsername);
        txtPassword = view.findViewById(R.id.txtPassword);
        txtRegister = view.findViewById(R.id.txtRegister);
        btnLogin = view.findViewById(R.id.btnLogin);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        txtRegister.setClickable(true);
        setEventListeners();
    }

    private void setEventListeners() {
        btnLogin.setOnClickListener(e -> {
            // Submit username and password to API client
            String username = txtUsername.getText().toString();
            String password = txtPassword.getText().toString();

            try {
                JSONObject credentials = new JSONObject() {{
                    put("username", username);
                    put("password", password);
                }};

                StringRequest request = new StringRequest(Request.Method.POST, Endpoints.URL + "/login",
                        null, foobar -> onLogin(false, "Format or credentials is incorrect")) {
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
                        CacheSingleton.getInstance(getContext()).saveToCache("token",
                                response.headers.get("Authorization").split(" ")[1]);
                        onLogin(true, "Logged in!");
                        return super.parseNetworkResponse(response);
                    }
                };

                VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
            } catch (Exception foo) {
                onLogin(false, "Failed to login");
                foo.printStackTrace();
            }
        });

        txtPassword.setOnKeyListener((view, keyCode, e) -> {
            // If the 'ENTER' key has been pressed on keyboard,
            // programmatically click on login button
            if (e.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                btnLogin.performClick();
            }

            return true;
        });

        txtRegister.setOnClickListener(e -> {
            // Redirect client to the registration page
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, regFragment)
                    .commit();
        });
    }


    public void setRegFragment(RegisterFragment regFragment) {
        this.regFragment = regFragment;
    }

    @Override
    public void onLogin(boolean success, String msg) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                // Do some GUI tasks on UI thread (to prevent exceptions)
                btnLogin.setEnabled(true);  // Enable button after task is finished
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            });
        }

        if (success) {
            this.startActivity(new Intent(getActivity(), MainActivity.class));
        }
    }
}
