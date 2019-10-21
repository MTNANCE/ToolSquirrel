package no.purplecloud.toolsquirrel.ui.credentials;

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

import no.purplecloud.toolsquirrel.R;
import no.purplecloud.toolsquirrel.async.LoginTask;
import no.purplecloud.toolsquirrel.listener.LoginListener;

public class LoginFragment extends Fragment implements LoginListener {

    private TextView txtUsername;
    private TextView txtPassword;
    private TextView txtRegister;
    private Button btnLogin;

    private RegisterFragment regFragment;

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

            // Disable button while request is ongoing
            btnLogin.setEnabled(false);

            // Preform login
            new LoginTask(username, password).setCallback(this).execute();
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
            // TODO Proceed to next activity
        }
    }

    public void setRegFragment(RegisterFragment regFragment) {
        this.regFragment = regFragment;
    }
}
