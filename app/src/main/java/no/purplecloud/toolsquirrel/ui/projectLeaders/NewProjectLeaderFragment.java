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

import no.purplecloud.toolsquirrel.R;
import no.purplecloud.toolsquirrel.domain.Employee;
import no.purplecloud.toolsquirrel.domain.Project;
import no.purplecloud.toolsquirrel.network.VolleySingleton;

public class NewProjectLeaderFragment extends Fragment {

    private AutoCompleteTextView autoCompleteEmployee;
    private AutoCompleteTextView autoCompleteProject;
    private TextView status;
    private Button submitBtn;

    // Change this to be of type Employee
    private String selectedEmployee;
    // Change this to be of type Project
    private String selectedProject;

    private String[] dummy = {
            "Aron Nicholasson (aronmar@live.no)",
            "Liban Nor (libanbn@gmail.com)",
            "Trygve Bærtrum (trygve@live.no)",
            "Dudleif Rompesaft (dudleif@rompesaft.no",
            "Satanistiske kora (kora@guderminfrelser.com",
            "Gaute Gravlagt (gauteburdebegraves@live.no"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_project_leader, container, false);
        this.autoCompleteEmployee = rootView.findViewById(R.id.add_leader_employee_selector);
        this.autoCompleteProject = rootView.findViewById(R.id.add_leader_project_selector);
        this.status = rootView.findViewById(R.id.add_leader_status);
        this.submitBtn = rootView.findViewById(R.id.add_leader_button);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, dummy);
        // Define the threshold point where it is going to start searching
        this.autoCompleteEmployee.setThreshold(1);
        this.autoCompleteProject.setThreshold(1);
        // Attach adapter
        this.autoCompleteEmployee.setAdapter(adapter);
        this.autoCompleteProject.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.autoCompleteEmployee.setOnItemClickListener((adapterView, view, i, l) -> {
            this.selectedEmployee = adapterView.getItemAtPosition(i).toString();
            System.out.println("Selected Employee: " + this.selectedEmployee);
        });
        this.autoCompleteProject.setOnItemClickListener(((adapterView, view, i, l) -> {
            this.selectedProject = adapterView.getItemAtPosition(i).toString();
            System.out.println("Selected Project: " + this.selectedProject);
        }));


        this.submitBtn.setOnClickListener(event -> {
            if (this.selectedEmployee != null && this.selectedProject != null) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("employee_id", this.selectedEmployee);
                    jsonObject.put("project_id", this.selectedProject);

                    VolleySingleton.getInstance(getContext()).postRequest("http://localhost:8080/addProjectLeaderToProject", jsonObject,
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
        });

    }
}
