package no.purplecloud.toolsquirrel.ui.projectLeaders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.squareup.picasso.Picasso;

import no.purplecloud.toolsquirrel.R;
import no.purplecloud.toolsquirrel.ui.manageEmployees.ManageEmployeesViewModel;

public class ProjectLeaderDetailsFragment extends Fragment {

    private ImageView image;
    private TextView employeeName;
    private TextView employeePosition;
    private TextView employeeEmployer;
    private TextView employeeEmail;
    private TextView employeePhone;
    private TextView employeeNr;
    private Button removeEmployeeBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectLeadersViewModel projectLeadersViewModel = ViewModelProviders.of(this.getActivity()).get(ProjectLeadersViewModel.class);
        projectLeadersViewModel.getSelectedProjectLeader().observe(this, employee -> {
            Picasso.get().load(employee.getImage()).into(this.image);
            this.employeeName.setText(employee.getName());
            this.employeePosition.setText(employee.getPosition());
            this.employeeEmployer.setText("Currently not available");
            this.employeeEmail.setText(employee.getEmail());
            this.employeePhone.setText(String.valueOf(employee.getPhone()));
            this.employeeNr.setText(String.valueOf(employee.getId()));
            this.removeEmployeeBtn.setText("Remove leader from this project ");
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_details, container, false);
        this.image = view.findViewById(R.id.employee_details_image);
        this.employeeName = view.findViewById(R.id.employee_details_name);
        this.employeePosition = view.findViewById(R.id.employee_details_position);
        this.employeeEmployer = view.findViewById(R.id.employee_details_employer);
        this.employeeEmail = view.findViewById(R.id.employee_details_email);
        this.employeePhone = view.findViewById(R.id.employee_details_phone);
        this.employeeNr = view.findViewById(R.id.employee_details_employee_number);
        this.removeEmployeeBtn = view.findViewById(R.id.manage_employees_remove_button);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.removeEmployeeBtn.setOnClickListener(event -> {
            Toast.makeText(getContext(), "Implement removal of this employee from selected project", Toast.LENGTH_LONG).show();
            // TODO Implement removal behaviour here
        });
    }
}