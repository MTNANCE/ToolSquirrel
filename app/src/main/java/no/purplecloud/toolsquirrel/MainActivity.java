package no.purplecloud.toolsquirrel;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.nio.file.Files;

import no.purplecloud.toolsquirrel.domain.Employee;
import no.purplecloud.toolsquirrel.singleton.CacheSingleton;
import no.purplecloud.toolsquirrel.ui.home.HomeViewModel;
import no.purplecloud.toolsquirrel.ui.manageEmployees.ManageEmployeesViewModel;
import no.purplecloud.toolsquirrel.ui.project.ProjectViewModel;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File cacheFile = new File(getApplicationContext().getCacheDir(), "cache_file.txt");
        if (!cacheFile.exists()) {
            this.startActivity(new Intent(getBaseContext(), CredentialsActivity.class));
        } else {
            if (!CacheSingleton.getInstance(getApplicationContext()).getFileContentAsJSON("cache_file.txt").has("token")) {
                this.startActivity(new Intent(this, CredentialsActivity.class));
            } else {
                setContentView(R.layout.activity_main);

                Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                NavigationView navigationView = findViewById(R.id.nav_view);
                // Passing each menu ID as a set of Ids because each
                // menu should be considered as top level destinations.
                mAppBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.nav_home, R.id.nav_rent, R.id.nav_return, R.id.nav_loans, R.id.nav_profile,
                        R.id.nav_projects, R.id.nav_manage_project_employees, R.id.nav_manage_tools)
                        .setDrawerLayout(drawer)
                        .build();

                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
                NavigationUI.setupWithNavController(navigationView, navController);
                // Set navigation actions
                ViewModelProviders.of(this).get(HomeViewModel.class).getSelectedTool().observe(this, selected ->
                        navController.navigate(R.id.action_nav_home_to_tool_details));
                ViewModelProviders.of(this).get(ProjectViewModel.class).getSelectedProject().observe(this, selected ->
                        navController.navigate(R.id.action_nav_projects_to_project_details));
                ViewModelProviders.of(this).get(ManageEmployeesViewModel.class).getSelectedEmployee().observe(this, selected ->
                        navController.navigate(R.id.action_nav_manage_project_employees_to_employee_details));

                // Get header view
                View headerView = navigationView.getHeaderView(0);
                TextView authenticatedUserName = headerView.findViewById(R.id.header_main_name);
                ImageView authenticatedUserImage = headerView.findViewById(R.id.header_main_image);
                // Get authenticated user
                Employee authenticatedUser = CacheSingleton.getInstance(getApplicationContext()).getAuthenticatedUser();
                // Set the header values
                authenticatedUserName.setText(authenticatedUser.getName());
                Picasso.get().load(authenticatedUser.getImage()).into(authenticatedUserImage);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                CacheSingleton.getInstance(getApplicationContext()).removeToken();
                startActivity(new Intent(this, CredentialsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
