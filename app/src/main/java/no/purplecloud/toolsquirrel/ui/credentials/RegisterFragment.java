package no.purplecloud.toolsquirrel.ui.credentials;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import no.purplecloud.toolsquirrel.R;

public class RegisterFragment extends Fragment {
    private LoginFragment loginFragment;

    private TextView txtUsername;
    private TextView txtPassword;
    private TextView txtCPassword;
    private TextView txtLogin;
    private Button btnRegister;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        txtUsername = view.findViewById(R.id.txtUsername);
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
            return true;
        });

        btnRegister.setOnClickListener(e -> {
            boolean passwordIsEqual = isPasswordEqual(txtPassword.getText().toString(),
                    txtCPassword.getText().toString());

            if (!passwordIsEqual) {
                Toast.makeText(getContext(), "Passwords aren't equal", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // TODO Impl registration handling
            }
        });
    }

    public void setLoginFragment(LoginFragment loginFragment) {
        this.loginFragment = loginFragment;
    }

    private boolean isPasswordEqual(String password1, String password2) {
        return password1.equals(password2);
    }
}