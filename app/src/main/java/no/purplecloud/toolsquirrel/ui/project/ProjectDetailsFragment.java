package no.purplecloud.toolsquirrel.ui.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.squareup.picasso.Picasso;

import no.purplecloud.toolsquirrel.R;

public class ProjectDetailsFragment extends Fragment {

    private ImageView image;
    private TextView title;
    private TextView description;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectViewModel projectViewModel = ViewModelProviders.of(this.getActivity()).get(ProjectViewModel.class);
        projectViewModel.getSelectedProject().observe(this, project -> {
            Picasso.get().load(project.getProjectImage()).into(this.image);
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
        return result;
    }
}
