package no.purplecloud.toolsquirrel;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import java.io.File;

import no.purplecloud.toolsquirrel.singleton.CacheSingleton;
import no.purplecloud.toolsquirrel.ui.credentials.LoginFragment;
import no.purplecloud.toolsquirrel.ui.credentials.RegisterFragment;

public class CredentialsActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File cacheFile = new File(getApplicationContext().getCacheDir(), "cache_file.txt");
        if (cacheFile.exists() &&
                CacheSingleton.getInstance(getApplicationContext()).getFileContentAsJSON("cache_file.txt").has("token")) {
            this.startActivity(new Intent(this, MainActivity.class));
        } else {
            setContentView(R.layout.credentials_main);

            LoginFragment loginFragment = new LoginFragment();
            RegisterFragment regFragment = new RegisterFragment();

            loginFragment.setRegFragment(regFragment);
            regFragment.setLoginFragment(loginFragment);

            // Startup login fragment as default fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, loginFragment)
                    .commit();
        }
    }
}
