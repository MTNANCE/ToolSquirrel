package no.purplecloud.toolsquirrel;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import no.purplecloud.toolsquirrel.async.LoginTask;
import no.purplecloud.toolsquirrel.listener.LoginListener;

public class LoginActivity extends AppCompatActivity implements LoginListener {
    private TextView txtUsername;
    private TextView txtPassword;
    private Button btnLogin;

    private TextView txtRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        // Get view instances
        txtUsername =   findViewById(R.id.txtUsername);
        txtPassword =   findViewById(R.id.txtPassword);
        btnLogin =      findViewById(R.id.btnLogin);

        txtRegister = findViewById(R.id.txtRegister);

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
            // If the 'ENTER' key has been pressed, programmatically click on login button
            if (e.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                btnLogin.performClick();
            }

            return true;
        });

        txtRegister.setClickable(true);
        txtRegister.setOnClickListener(e -> {
            // TODO Goto registration activity
            Toast.makeText(this, "Registration...", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onLogin(boolean success, String msg) {
        runOnUiThread(() -> {
            // Do some GUI tasks on UI thread (to prevent exceptions)
            btnLogin.setEnabled(true);  // Enable button after task is finished
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });

        if (success) {
            // TODO Proceed to next activity
        }
    }
}
