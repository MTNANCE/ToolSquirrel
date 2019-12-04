package no.purplecloud.toolsquirrel.ui.projectLeaders;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import no.purplecloud.toolsquirrel.Endpoints;
import no.purplecloud.toolsquirrel.domain.Employee;
import no.purplecloud.toolsquirrel.domain.Project;
import no.purplecloud.toolsquirrel.domain.Tool;
import no.purplecloud.toolsquirrel.network.VolleySingleton;
import no.purplecloud.toolsquirrel.singleton.CacheSingleton;

public class ProjectLeadersViewModel extends AndroidViewModel {

    // Observable list of tools
    private MutableLiveData<List<Employee>> listOfProjectLeaders;
    // Observable selected project leader
    private MutableLiveData<Employee> selectedProjectLeader = new MutableLiveData<>();

    // We need the selected project to identify which project leaders are assigned to it
    private Long selectedProject;

    private Context context;

    public ProjectLeadersViewModel(Application context) {
        super(context);
        this.context = context;
    }

    public void setSelectedLeader(Employee employee) {
        this.selectedProjectLeader.setValue(employee);
    }

    public MutableLiveData<Employee> getSelectedProjectLeader() {
        return this.selectedProjectLeader;
    }

    public LiveData<List<Employee>> getProjectLeaders() {
        if (this.listOfProjectLeaders == null) {
            this.listOfProjectLeaders = new MutableLiveData<>();
            getAllProjectLeaders();
        }
        return this.listOfProjectLeaders;
    }

    public void getAllProjectLeaders() {
        String selectedProjectId = CacheSingleton.getInstance(context).loadFromData("selected_project");
        System.out.println("Selected Project Id: " + selectedProjectId + (" (Request)"));
        if (!selectedProjectId.isEmpty() && selectedProjectId != null) {
            VolleySingleton.getInstance(this.context)
                    .searchGetRequest(Endpoints.URL + "/findProjectLeaders/", selectedProjectId, "employee", listOfProjectLeaders::setValue);
        }
    }

    public void setListOfProjectLeaders(List<Employee> list) {
        this.listOfProjectLeaders.setValue(list);
    }

}
