package no.purplecloud.toolsquirrel.ui.loan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import no.purplecloud.toolsquirrel.R;

public class LoansFragment extends Fragment {

    private LoansViewModel loansViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // The root view where the recycler view is located (loans fragment)
        View rootView = inflater.inflate(R.layout.fragment_loans, container, false);
        // The recycler view
        RecyclerView recyclerView = rootView.findViewById(R.id.loans_recycler_view);
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
}
