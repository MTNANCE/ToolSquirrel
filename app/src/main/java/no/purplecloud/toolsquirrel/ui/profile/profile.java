package no.purplecloud.toolsquirrel.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import no.purplecloud.toolsquirrel.Endpoints;
import no.purplecloud.toolsquirrel.R;
import no.purplecloud.toolsquirrel.adapter.SpinnerAdapter;
import no.purplecloud.toolsquirrel.domain.Employee;
import no.purplecloud.toolsquirrel.domain.Project;
import no.purplecloud.toolsquirrel.network.VolleySingleton;
import no.purplecloud.toolsquirrel.singleton.CacheSingleton;


public class profile extends Fragment {

    private ImageView profileImage;
    private TextView profileName;
    private TextView profileEmail;
    private TextView profilePhone;

    private Spinner spinner;

    private List<Project> listOfProjects = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        // Set widgets
        this.profileImage = rootView.findViewById(R.id.profile_image);
        this.profileName = rootView.findViewById(R.id.profile_name);
        this.profileEmail = rootView.findViewById(R.id.profile_email);
        this.profilePhone = rootView.findViewById(R.id.profile_phone);
        // Spinner related
        this.spinner = rootView.findViewById(R.id.profile_spinner);
        // Get authenticated user
        Employee authenticatedUser = CacheSingleton.getInstance(getContext()).getAuthenticatedUser();
        VolleySingleton.getInstance(getContext())
                .getListRequest(Endpoints.URL +"/findAllProjectsThatUserIsIn/" + authenticatedUser.getId(), "project", list -> {
                    this.listOfProjects = list;
                    // Set profile information
                    Picasso.get().load(authenticatedUser.getImage()).into(this.profileImage);
                    this.profileName.setText(authenticatedUser.getName());
                    this.profileEmail.setText(authenticatedUser.getEmail());
                    this.profilePhone.setText(String.valueOf(authenticatedUser.getPhone()));
                    // Custom spinner adapter
                    SpinnerAdapter adapter = new SpinnerAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, this.listOfProjects);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    this.spinner.setAdapter(adapter);


                    // Check if there already is an active project
                    if (!CacheSingleton.getInstance(getContext()).loadFromData("activeProject").trim().equals("") &&
                            CacheSingleton.getInstance(getContext()).loadFromData("activeProject") != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(CacheSingleton.getInstance(getContext()).loadFromData("activeProject"));
                            this.spinner.setSelection(jsonObject.getInt("position"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Project predefinedProject = this.listOfProjects.get(0);
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("id", predefinedProject.getProjectId());
                            jsonObject.put("name", predefinedProject.getProjectName());
                            jsonObject.put("position", 0);
                            CacheSingleton.getInstance(getContext()).saveToData("activeProject", jsonObject.toString());
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
                                CacheSingleton.getInstance(getContext()).saveToData("activeProject", jsonObject.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
