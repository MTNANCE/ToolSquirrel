package no.purplecloud.toolsquirrel.ui.credentials;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class RegisterFragment extends Fragment implements LoginListener {
    private LoginFragment loginFragment;

    private EditText txtUsername;
    private EditText txtName;
    private EditText txtEmail;
    private EditText txtPhone;
    private EditText txtPassword;
    private EditText txtCPassword;

    private TextView txtLogin;
    private Button btnRegister;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        txtUsername = view.findViewById(R.id.txtUsername);
        txtName = view.findViewById(R.id.txtName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtPassword = view.findViewById(R.id.txtPassword);
        txtCPassword = view.findViewById(R.id.txtCPassword);

        btnRegister = view.findViewById(R.id.btnRegister);
        txtLogin = view.findViewById(R.id.txtLogin);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        txtLogin.setClickable(true);
        setEventListeners();
    }

    private void setEventListeners() {
        txtLogin.setOnClickListener(e -> {
            // // Redirect client to the registration page
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, loginFragment)
                    .commit();
        });

        txtCPassword.setOnKeyListener((view, key, e) -> {
            // If the 'ENTER' key has been pressed on keyboard,
            // programmatically click on register button (after 'confirm password' and password
            // is equal
            if (e.getAction() == KeyEvent.ACTION_DOWN && key == KeyEvent.KEYCODE_ENTER) {
                btnRegister.performClick();
            }
            if (e.getAction() == KeyEvent.ACTION_UP && key == KeyEvent.KEYCODE_DEL) {
                int length = txtCPassword.getText().length();
                if (!txtCPassword.getText().toString().isEmpty() && length > 0) {
                    txtCPassword.getText().delete(length - 1, length);
                }
            }
            return true;
        });

        btnRegister.setOnClickListener(e -> {
            boolean passwordIsEqual = isPasswordEqual(txtPassword.getText().toString(),
                    txtCPassword.getText().toString());

            if (!passwordIsEqual) {
                Toast.makeText(getContext(), "Passwords aren't equal", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // Submit username and password to API client
                String username = txtUsername.getText().toString();
                String name = txtName.getText().toString();
                String email = txtEmail.getText().toString();
                int phone = Integer.valueOf(txtPhone.getText().toString());
                String password = txtPassword.getText().toString();

                try {
                    JSONObject credentials = new JSONObject() {{
                        put("username", username);
                        put("name", name);
                        put("email", email);
                        put("phone", phone);
                        put("password", password);
                    }};

                    StringRequest request = new StringRequest(Request.Method.POST, Endpoints.URL + "/signup",
                            null, foobar -> onLogin(false, foobar.getMessage())) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            return new HashMap<String, String>() {{
                                put("Content-Type", "application/json");
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
                            onLogin(true, "Registered!");
                            return super.parseNetworkResponse(response);
                        }
                    };

                    VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
                } catch (Exception foo) {
                    onLogin(false, "Failed to register");
                    foo.printStackTrace();
                }
            }
        });
    }

    public void setLoginFragment(LoginFragment loginFragment) {
        this.loginFragment = loginFragment;
    }

    private boolean isPasswordEqual(String password1, String password2) {
        return password1.equals(password2);
    }

    @Override
    public void onLogin(boolean success, String msg) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                // Do some GUI tasks on UI thread (to prevent exceptions)
                btnRegister.setEnabled(true);  // Enable button after task is finished
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            });
        }

        if (success) {
            this.startActivity(new Intent(getActivity(), MainActivity.class));
        }
    }
}