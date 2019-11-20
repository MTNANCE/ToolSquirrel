package no.purplecloud.toolsquirrel.ui.profile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.purplecloud.toolsquirrel.R;
import no.purplecloud.toolsquirrel.domain.Project;
import no.purplecloud.toolsquirrel.singleton.CacheSingleton;


public class profile extends Fragment {

    private Spinner spinner;

    // TODO Remove this
    private List<Project> listOfProjects = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        // Spinner related
        this.spinner = rootView.findViewById(R.id.profile_spinner);
        // Create dummy data
        this.listOfProjects.add(new Project(1L, "Test Project", "Test desc", "", "test location"));
        this.listOfProjects.add(new Project(2L, "amøbe", "test", "cyka", "travo"));

        // Custom spinner adapter
        SpinnerAdapter adapter = new SpinnerAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, this.listOfProjects);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        this.spinner.setAdapter(adapter);
        // Check if there already is an active project
        if (!CacheSingleton.getInstance(getContext()).loadFromCache("activeProject").trim().equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(CacheSingleton.getInstance(getContext()).loadFromCache("activeProject"));
                this.spinner.setSelection(jsonObject.getInt("position"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // Set listener
        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Project selectedProject = adapter.getItem(position);
                try {
                    // TODO Maybe add more active project details?
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", selectedProject.getProjectId());
                    jsonObject.put("name", selectedProject.getProjectName());
                    jsonObject.put("position", position);
                    CacheSingleton.getInstance(getContext()).saveToCache("activeProject", jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
