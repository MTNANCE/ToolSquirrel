package no.purplecloud.toolsquirrel.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import no.purplecloud.toolsquirrel.R;
import no.purplecloud.toolsquirrel.domain.Tool;
import no.purplecloud.toolsquirrel.singleton.CacheSingleton;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private SearchView searchField;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // The root view where the recycler view is located (home fragment)
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // The recycler view
        RecyclerView recyclerView = rootView.findViewById(R.id.home_recycler_view);
        // SearchView
        this.searchField = rootView.findViewById(R.id.home_search);
        // Linear layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        // Divider decoration to set lines between each recycled view
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        this.homeViewModel = ViewModelProviders.of(this.getActivity()).get(HomeViewModel.class);
        this.homeViewModel.getTools().observe(this, tools ->
                recyclerView.setAdapter(new ToolListRecyclerViewAdapter(tools)));
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
                    homeViewModel.getTools();
                } else {
                    homeViewModel.searchForTools(search);
                }
                return true;
            }
        });
    }
}