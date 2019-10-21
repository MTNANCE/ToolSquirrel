package no.purplecloud.toolsquirrel.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import no.purplecloud.toolsquirrel.R;
import no.purplecloud.toolsquirrel.domain.Tool;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // The root view where the recycler view is located (home fragment)
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // The recycler view
        RecyclerView recyclerView = rootView.findViewById(R.id.home_recycler_view);
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

}