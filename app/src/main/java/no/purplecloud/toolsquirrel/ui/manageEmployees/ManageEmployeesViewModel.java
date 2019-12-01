package no.purplecloud.toolsquirrel.ui.manageEmployees;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import no.purplecloud.toolsquirrel.domain.Employee;
import no.purplecloud.toolsquirrel.network.VolleySingleton;

public class ManageEmployeesViewModel extends AndroidViewModel {

    // Observable list of employees
    private MutableLiveData<List<Employee>> listOfEmployees;
    // Observable selected employee
    private MutableLiveData<Employee> selectedEmployee = new MutableLiveData<>();

    private Long selectedProject;

    private Context context;

    public ManageEmployeesViewModel(Application context) {
        super(context);
        this.context = context;
    }

    public void setSelectedEmployee(Employee employee) {
        this.selectedEmployee.setValue(employee);
    }

    public MutableLiveData<Employee> getSelectedEmployee() {
        return this.selectedEmployee;
    }

    public LiveData<List<Employee>> getEmployees() {
        if (this.listOfEmployees == null) {
            this.listOfEmployees = new MutableLiveData<>();
        }
        return this.listOfEmployees;
    }

    public void setListOfEmployees(List<Employee> list) {
        this.listOfEmployees.setValue(list);
    }

    public void setSelectedProject(Long selectedProject) {
        this.selectedProject = selectedProject;
    }

}
