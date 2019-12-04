package no.purplecloud.toolsquirrel.ui.projectLeaders;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import no.purplecloud.toolsquirrel.Endpoints;
import no.purplecloud.toolsquirrel.R;
import no.purplecloud.toolsquirrel.domain.Employee;
import no.purplecloud.toolsquirrel.domain.Project;
import no.purplecloud.toolsquirrel.network.VolleySingleton;
import no.purplecloud.toolsquirrel.singleton.CacheSingleton;

public class NewProjectLeaderFragment extends Fragment {

    private AutoCompleteTextView autoCompleteEmployee;
    private TextView status;
    private Button submitBtn;

    // Change this to be of type Employee
    private String selectedEmployee;

    private List<String> employeeList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_project_leader, container, false);
        this.autoCompleteEmployee = rootView.findViewById(R.id.add_leader_employee_selector);
        this.status = rootView.findViewById(R.id.add_leader_status);
        this.submitBtn = rootView.findViewById(R.id.add_leader_button);

        // Get selected project
        String selectedProjectId = CacheSingleton.getInstance(getContext()).loadFromData("selected_project");
        // Setup Employee AutoComplete
        VolleySingleton.getInstance(getContext())
                .getListRequest(Endpoints.URL + "/employees/notProjectLeaders/" + selectedProjectId, "employee",
                        foo -> {
                            for (Object object : foo) {
                                if (object instanceof Employee) {
                                    this.employeeList.add(((Employee) object).getName() + " #" + ((Employee) object).getId());
                                }
                            }
                            ArrayAdapter<String> employeeAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, employeeList);
                            // Define the threshold point where it is going to start searching
                            this.autoCompleteEmployee.setThreshold(1);
                            // Attach adapter
                            this.autoCompleteEmployee.setAdapter(employeeAdapter);
                        }
                );
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.autoCompleteEmployee.setOnItemClickListener((adapterView, view, i, l) -> {
            this.selectedEmployee = adapterView.getItemAtPosition(i).toString();
            System.out.println("Selected Employee: " + this.selectedEmployee);
        });

        this.submitBtn.setOnClickListener(event -> {
            String selectedProjectId = CacheSingleton.getInstance(getContext()).loadFromData("selected_project");
            if (!selectedProjectId.isEmpty() && selectedProjectId != null) {
                if (this.selectedEmployee != null) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("employee_id", this.selectedEmployee.split("#")[1]);
                        jsonObject.put("project_id", selectedProjectId);
                        VolleySingleton.getInstance(getContext()).postRequest(Endpoints.URL + "/addProjectLeaderToProject", jsonObject,
                                response -> {
                                    this.status.setText("Successfully added new project leader!");
                                    this.status.setTextColor(Color.parseColor("#1fa139"));
                                }, error -> {
                                    this.status.setText("Failed to add new project leader.");
                                    this.status.setTextColor(Color.parseColor("#e01919"));
                                }
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}
