package no.purplecloud.toolsquirrel.ui.projectLeaders;

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

import org.json.JSONException;
import org.json.JSONObject;

import no.purplecloud.toolsquirrel.Endpoints;
import no.purplecloud.toolsquirrel.R;
import no.purplecloud.toolsquirrel.network.VolleySingleton;
import no.purplecloud.toolsquirrel.singleton.CacheSingleton;
import no.purplecloud.toolsquirrel.ui.loan.LoanListRecyclerViewAdapter;
import no.purplecloud.toolsquirrel.ui.loan.LoansViewModel;

public class ProjectLeadersFragment extends Fragment {

    private ProjectLeadersViewModel projectLeadersViewModel;

    private SearchView searchField;

    private FloatingActionButton fab;

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // The root view where the recycler view is located
        View rootView = inflater.inflate(R.layout.fragment_project_leaders, container, false);
        // The recycler view
        RecyclerView recyclerView = rootView.findViewById(R.id.project_leaders_recycler_view);
        // SearchView
        this.searchField = rootView.findViewById(R.id.project_leaders_search);
        // Floating Action Add Button
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        fab = rootView.findViewById(R.id.project_leaders_fab);
        // Linear layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        // Divider decoration to set lines between each recycled view
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        this.projectLeadersViewModel = ViewModelProviders.of(this.getActivity()).get(ProjectLeadersViewModel.class);
        this.projectLeadersViewModel.getProjectLeaders().observe(this, leaders ->
                recyclerView.setAdapter(new ProjectLeadersRecyclerAdapter(leaders)));
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.fab.setOnClickListener(event -> {
            // Redirect client to the add new project leader fragment
            navController.navigate(R.id.action_nav_project_leaders_to_add_new_project_leader);
        });
        this.searchField.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String search) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String search) {
                String selectedProjectId = CacheSingleton.getInstance(getContext()).loadFromData("selected_project");
                if (!selectedProjectId.isEmpty() && selectedProjectId != null) {
                    if (search.trim().equals("")) {
                        VolleySingleton.getInstance(getContext())
                                .searchGetRequest(Endpoints.URL + "/findProjectLeaders/",
                                        selectedProjectId, "employee",
                                        list -> projectLeadersViewModel.setListOfProjectLeaders(list));
                    } else {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("search", search);
                            jsonObject.put("project_id", selectedProjectId);
                            VolleySingleton.getInstance(getContext())
                                    .searchPostRequestWithBody(Endpoints.URL + "/searchAllProjectLeaders",
                                            jsonObject, "employee",
                                            list -> projectLeadersViewModel.setListOfProjectLeaders(list));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return true;
            }
        });
    }
}
