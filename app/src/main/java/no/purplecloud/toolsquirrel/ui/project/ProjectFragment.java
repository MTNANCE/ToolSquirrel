package no.purplecloud.toolsquirrel.ui.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import no.purplecloud.toolsquirrel.R;

public class ProjectFragment extends Fragment {

    private ProjectViewModel projectViewModel;

    private SearchView searchField;

    private FloatingActionButton fab;

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // The root view where the recycler view is located (home fragment)
        View rootView = inflater.inflate(R.layout.fragment_projects, container, false);
        // The recycler view
        RecyclerView recyclerView = rootView.findViewById(R.id.projects_recycler_view);
        // SearchView
        this.searchField = rootView.findViewById(R.id.projects_search);
        // Floating Action Add Button
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        fab = rootView.findViewById(R.id.fab);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.searchField.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String search) {
                System.out.println("Search Input: " + search);
                if (search.trim().equals("")) {
                    projectViewModel.getProjects();
                } else {
                    projectViewModel.searchForProjects(search);
                }
                return true;
            }
        });
        this.fab.setOnClickListener(event -> {
            // Redirect client to the new project fragment
            navController.navigate(R.id.action_nav_projects_to_add_new_project);
        });
    }
}
