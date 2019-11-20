package no.purplecloud.toolsquirrel.ui.manageTools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import no.purplecloud.toolsquirrel.ui.manageEmployees.ManageEmployeesRecyclerAdapter;
import no.purplecloud.toolsquirrel.ui.manageEmployees.ManageEmployeesViewModel;

public class ManageToolsFragment extends Fragment {

    private ManageToolsViewModel manageToolsViewModel;
    private SearchView searchView;
    private NavController navController;
    private AutoCompleteTextView autoCompleteProjectSearch;
    private FloatingActionButton fab;

    private String selectedProject;

    // TODO Remove this
    private String[] dummy = {
            "Stadia (#1031548)",
            "Vertex (#1341323)"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manage_tools, container, false);
        // The recycler view
        RecyclerView recyclerView = rootView.findViewById(R.id.manage_tools_recycler_view);
        // SearchView
        this.searchView = rootView.findViewById(R.id.manage_tools_search);
        // AutoCompleteSearchField
        this.autoCompleteProjectSearch = rootView.findViewById(R.id.manage_tools_project_search);
        // Get NavController
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        // Floating Action Add Button
        fab = rootView.findViewById(R.id.manage_tools_fab);
        // Linear layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        // Divider decoration to set lines between each recycled view
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        this.manageToolsViewModel = ViewModelProviders.of(this.getActivity()).get(ManageToolsViewModel.class);
        this.manageToolsViewModel.getTools().observe(this, tools ->
                recyclerView.setAdapter(new ManageToolRecyclerAdapter(tools)));

        // Setup AutoComplete Related stuff TODO Change type of adapter (probably)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, dummy);
        // Define the threshold point where it is going to start searching
        this.autoCompleteProjectSearch.setThreshold(1);
        // Attach adapter
        this.autoCompleteProjectSearch.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.fab.setOnClickListener(event -> {
            navController.navigate(R.id.action_nav_manage_tools_to_add_new_tool);
        });
    }
}
