package no.purplecloud.toolsquirrel.ui.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.squareup.picasso.Picasso;

import no.purplecloud.toolsquirrel.R;

public class ProjectDetailsFragment extends Fragment {

    private ImageView image;
    private TextView title;
    private TextView description;

    private Button manageBtn;

    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectViewModel projectViewModel = ViewModelProviders.of(this.getActivity()).get(ProjectViewModel.class);
        projectViewModel.getSelectedProject().observe(this, project -> {
            if (!project.getProjectImage().trim().equals("")) {
                Picasso.get().load(project.getProjectImage()).into(this.image);
            } else {
                Picasso.get().load("https://pixselo.com/wp-content/uploads/2018/03/dummy-placeholder-image-400x400.jpg").into(this.image);
            }

            // Set activity header
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(project.getProjectName());
            this.title.setText(project.getProjectName());
            this.description.setText(project.getProjectDescription());
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_project_details, container, false);
        this.image = result.findViewById(R.id.project_details_image);
        this.title = result.findViewById(R.id.project_details_tile);
        this.description = result.findViewById(R.id.project_details_desc);
        this.navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        this.manageBtn = result.findViewById(R.id.project_details_manage_button);
        return result;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.manageBtn.setOnClickListener(event -> {
            // Redirect client to the new project fragment
            navController.navigate(R.id.action_nav_project_details_to_project_leaders);
        });
    }
}
