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

import no.purplecloud.toolsquirrel.domain.Employee;
import no.purplecloud.toolsquirrel.domain.Project;
import no.purplecloud.toolsquirrel.network.VolleySingleton;

public class ProjectLeadersViewModel extends AndroidViewModel {

    // Observable list of tools
    private MutableLiveData<List<Employee>> listOfProjectLeaders;

    // We need the selected project to identify which project leaders are assigned to it
    private Long selectedProject;

    private Context context;

    public ProjectLeadersViewModel(Application context) {
        super(context);
        this.context = context;
    }

    public LiveData<List<Employee>> getProjectLeaders() {
        if (this.listOfProjectLeaders == null) {
            this.listOfProjectLeaders = new MutableLiveData<>();
            getAllProjectLeaders();
        }
        return this.listOfProjectLeaders;
    }

    public void getAllProjectLeaders() {
        String url = "http://192.168.1.97:8080/findProjectLeaders/" + this.selectedProject;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, null,
            response -> {
                List<Employee> projectLeaders = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        projectLeaders.add(new Employee(response.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                this.listOfProjectLeaders.setValue(projectLeaders);
            }, error -> {
                System.out.println("ERROR: " + error);
                // TODO Create dummy data for now
                ArrayList<Employee> dummy = new ArrayList<>();
                dummy.add(new Employee(1L, "Trygve Danielsen", "tepådo123", "trygve@danielsen.com", 46879452, "https://i1.rgstatic.net/ii/profile.image/272746844258324-1442039323690_Q512/Trygve_Sigholt.jpg", new ArrayList<>(), new ArrayList<>(), "Manager"));
                this.listOfProjectLeaders.setValue(dummy);
            }
        );
        VolleySingleton.getInstance(this.context).addToRequestQueue(jsonArrayRequest);
    }

    public void setSelectedProject(Long selectedProject) {
        this.selectedProject = selectedProject;
    }
}
