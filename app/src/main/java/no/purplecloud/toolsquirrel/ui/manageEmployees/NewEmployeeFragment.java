package no.purplecloud.toolsquirrel.ui.manageEmployees;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import no.purplecloud.toolsquirrel.Endpoints;
import no.purplecloud.toolsquirrel.R;
import no.purplecloud.toolsquirrel.domain.Employee;
import no.purplecloud.toolsquirrel.domain.Project;
import no.purplecloud.toolsquirrel.network.VolleySingleton;
import no.purplecloud.toolsquirrel.singleton.CacheSingleton;

public class NewEmployeeFragment extends Fragment {

    private AutoCompleteTextView autoCompleteEmployee;
    private AutoCompleteTextView autoCompleteProject;
    private TextView status;
    private Button submitBtn;

    // Change this to be of type Employee
    private String selectedEmployee;
    // Change this to be of type Project
    private String selectedProject;

    private List<String> employeeList = new ArrayList<>();

    private List<String> projectList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_employee, container, false);
        this.autoCompleteEmployee = rootView.findViewById(R.id.add_employee_selector);
        this.autoCompleteProject = rootView.findViewById(R.id.add_employee_project_selector);
        this.status = rootView.findViewById(R.id.add_employee_status);
        this.submitBtn = rootView.findViewById(R.id.add_employee_button);

        // Setup Employee AutoComplete
        VolleySingleton.getInstance(getContext())
                .getListRequest(Endpoints.URL + "/employees", "employee",
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
        // TODO This is some really dumb shit, change this shit to be some callback shit instead
        // Setup Project AutoComplete
        Long employee_id = CacheSingleton.getInstance(getContext()).getAuthenticatedUser().getId();
        VolleySingleton.getInstance(getContext())
                .getListRequest(Endpoints.URL + "/findAllProjectsUserIsLeaderFor/" + employee_id, "project", bar -> {
                    for (Object object : bar) {
                        if (object instanceof Project) {
                            this.projectList.add(((Project) object).getProjectName() + " #" + ((Project) object).getProjectId());
                        }
                    }
                    ArrayAdapter<String> projectAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, projectList);
                    // Define the threshold point where it is going to start searching
                    this.autoCompleteProject.setThreshold(1);
                    // Attach adapter
                    this.autoCompleteProject.setAdapter(projectAdapter);
                });

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

        // TODO Add onClick listener to submitBtn
    }
}
