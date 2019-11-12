package no.purplecloud.toolsquirrel.ui.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import no.purplecloud.toolsquirrel.R;

public class ProjectFragment extends Fragment {

    private ProjectViewModel projectViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // The root view where the recycler view is located (home fragment)
        View rootView = inflater.inflate(R.layout.fragment_projects, container, false);
        // The recycler view
        RecyclerView recyclerView = rootView.findViewById(R.id.projects_recycler_view);
        // Floating Action Add Button
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        // Linear layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        // Divider decoration to set lines between each recycled view
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        this.projectViewModel = ViewModelProviders.of(this.getActivity()).get(ProjectViewModel.class);
        this.projectViewModel.getProjects().observe(this, projects ->
                recyclerView.setAdapter(new ProjectListRecyclerAdapter(projects)));
        return rootView;
    }
}
