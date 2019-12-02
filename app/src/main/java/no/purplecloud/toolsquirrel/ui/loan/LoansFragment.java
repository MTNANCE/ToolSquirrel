package no.purplecloud.toolsquirrel.ui.loan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import no.purplecloud.toolsquirrel.Endpoints;
import no.purplecloud.toolsquirrel.R;
import no.purplecloud.toolsquirrel.domain.Employee;
import no.purplecloud.toolsquirrel.network.VolleySingleton;
import no.purplecloud.toolsquirrel.singleton.CacheSingleton;

public class LoansFragment extends Fragment {

    private LoansViewModel loansViewModel;
    private SearchView searchField;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // The root view where the recycler view is located (loans fragment)
        View rootView = inflater.inflate(R.layout.fragment_loans, container, false);
        // The recycler view
        RecyclerView recyclerView = rootView.findViewById(R.id.loans_recycler_view);
        // SearchView
        this.searchField = rootView.findViewById(R.id.loans_search);
        // Linear layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        // Divider decoration to set lines between each recycled view
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        this.loansViewModel = ViewModelProviders.of(this.getActivity()).get(LoansViewModel.class);
        this.loansViewModel.getLoans().observe(this, loans ->
                recyclerView.setAdapter(new LoanListRecyclerViewAdapter(loans)));
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Employee employee = CacheSingleton.getInstance(getContext()).getAuthenticatedUser();
        this.searchField.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String search) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String search) {
                System.out.println("SEARCH CHANGE!!!!!!!!!!!!!!!!!");
                if (search.trim().equals("")) {
                    VolleySingleton.getInstance(getContext()).getListRequest(Endpoints.URL + "/findAllBorrows/" + employee.getId(), "loan",
                            list -> loansViewModel.setListOfLoans(list));
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("search", search);
                        jsonObject.put("employee_id", employee.getId());
                        VolleySingleton.getInstance(getContext())
                                .searchPostRequestWithBody(Endpoints.URL + "/searchAllBorrows",
                                        jsonObject, "loan", list -> loansViewModel.setListOfLoans(list)
                                );

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
    }
}
