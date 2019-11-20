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

import no.purplecloud.toolsquirrel.R;

public class NewEmployeeFragment extends Fragment {

    private AutoCompleteTextView autoCompleteEmployee;
    private AutoCompleteTextView autoCompleteProject;
    private TextView status;
    private Button submitBtn;

    // Change this to be of type Employee
    private String selectedEmployee;
    // Change this to be of type Project
    private String selectedProject;

    // TODO Remove this
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
        View rootView = inflater.inflate(R.layout.fragment_new_employee, container, false);
        this.autoCompleteEmployee = rootView.findViewById(R.id.add_employee_selector);
        this.autoCompleteProject = rootView.findViewById(R.id.add_employee_project_selector);
        this.status = rootView.findViewById(R.id.add_employee_status);
        this.submitBtn = rootView.findViewById(R.id.add_employee_button);

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

        // TODO Add onClick listener to submitBtn
    }
}
