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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import no.purplecloud.toolsquirrel.Endpoints;
import no.purplecloud.toolsquirrel.R;
import no.purplecloud.toolsquirrel.domain.Project;
import no.purplecloud.toolsquirrel.network.VolleySingleton;
import no.purplecloud.toolsquirrel.singleton.CacheSingleton;
import no.purplecloud.toolsquirrel.ui.manageEmployees.ManageEmployeesRecyclerAdapter;
import no.purplecloud.toolsquirrel.ui.manageEmployees.ManageEmployeesViewModel;

public class ManageToolsFragment extends Fragment {

    private ManageToolsViewModel manageToolsViewModel;
    private SearchView searchView;
    private NavController navController;
    private AutoCompleteTextView autoCompleteProjectSearch;
    private FloatingActionButton fab;

    private String selectedProject;

    // TODO Hold on projects instead (need to create a custom adapter in that case)
    private List<String> projectList = new ArrayList();

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

        // Setup AutoComplete Related stuff
        Long employee_id = CacheSingleton.getInstance(getContext()).getAuthenticatedUser().getId();
        VolleySingleton.getInstance(getContext())
                .getListRequest(Endpoints.URL + "/findAllProjectsUserIsLeaderFor/" + employee_id, "project", list -> {
                    for (Object object : list) {
                        if (object instanceof Project) {
                            this.projectList.add(((Project) object).getProjectName() + " #" + ((Project) object).getProjectId());
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, projectList);
                    // Define the threshold point where it is going to start searching
                    this.autoCompleteProjectSearch.setThreshold(1);
                    // Attach adapter
                    this.autoCompleteProjectSearch.setAdapter(adapter);
                });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.fab.setOnClickListener(event -> {
            navController.navigate(R.id.action_nav_manage_tools_to_add_new_tool);
        });
        // AutoCompleteSelector
        this.autoCompleteProjectSearch.setOnItemClickListener(((adapterView, view, i, l) -> {
            this.selectedProject = adapterView.getItemAtPosition(i).toString();
            VolleySingleton.getInstance(getContext())
                    .getListRequest(Endpoints.URL + "/getAllUniqueToolsByProject/" + selectedProject.split("#")[1],
                            "tool", list -> manageToolsViewModel.setListOfTools(list));
        }));
        // Search bar (search for tools)
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String search) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String search) {
                if (search.trim().equals("")) {
                    VolleySingleton.getInstance(getContext())
                            .getListRequest(Endpoints.URL + "/getAllUniqueToolsByProject/" + selectedProject.split("#")[1],
                                    "tool", list -> manageToolsViewModel.setListOfTools(list));
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("search", search);
                        jsonObject.put("project_id", selectedProject.split("#")[1]);
                        if (!selectedProject.isEmpty()) {
                            VolleySingleton.getInstance(getContext())
                                    .searchPostRequestWithBody(Endpoints.URL + "/searchTool",
                                            jsonObject, "tool", list -> manageToolsViewModel.setListOfTools(list));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
    }
}
