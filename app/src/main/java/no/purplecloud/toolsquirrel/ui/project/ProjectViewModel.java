package no.purplecloud.toolsquirrel.ui.project;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import no.purplecloud.toolsquirrel.Endpoints;
import no.purplecloud.toolsquirrel.domain.Project;
import no.purplecloud.toolsquirrel.domain.Tool;
import no.purplecloud.toolsquirrel.network.VolleySingleton;
import no.purplecloud.toolsquirrel.singleton.CacheSingleton;

public class ProjectViewModel extends AndroidViewModel {

    // Observable list of projects
    private MutableLiveData<List<Project>> listOfProjects;
    // Observable selected project
    private MutableLiveData<Project> selectedProject = new MutableLiveData<>();

    private Context context;

    public ProjectViewModel(Application context) {
        super(context);
        this.context = context;
    }

    public void setSelectedProject(Project selectedProject) {
        this.selectedProject.setValue(selectedProject);
    }

    public MutableLiveData<Project> getSelectedProject() {
        return this.selectedProject;
    }

    public LiveData<List<Project>> getProjects() {
        if (this.listOfProjects == null) {
            this.listOfProjects = new MutableLiveData<>();
            getAllProjects();
        }
        return this.listOfProjects;
    }

    public void searchForProjects(String search) {
        try {
            // TODO Redundant?
            Long employee_id = CacheSingleton.getInstance(context).getAuthenticatedUser().getId();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("search", search);
            jsonObject.put("employee_id", employee_id);

            VolleySingleton.getInstance(this.context)
                    .searchPostRequestWithBody(Endpoints.URL + "/searchProject/", jsonObject, "project", listOfProjects::setValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getAllProjects() {
        Long employee_id = CacheSingleton.getInstance(context).getAuthenticatedUser().getId();
        VolleySingleton.getInstance(this.context)
                .getListRequest(Endpoints.URL + "/findAllProjectsUserIsLeaderFor/" + employee_id, "project", listOfProjects::setValue);
    }

}
