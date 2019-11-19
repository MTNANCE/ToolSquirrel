package no.purplecloud.toolsquirrel.ui.project;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import no.purplecloud.toolsquirrel.domain.Project;
import no.purplecloud.toolsquirrel.domain.Tool;
import no.purplecloud.toolsquirrel.network.VolleySingleton;

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
        List<Project> projects = VolleySingleton.getInstance(this.context)
                .searchPostRequest("http://192.168.1.97:8080/searchProject/", search, "project");
        this.listOfProjects.setValue(projects);
    }

    private void getAllProjects() {
        List<Project> projects = VolleySingleton.getInstance(this.context)
                .getListRequest("http://192.168.1.97:8080/findAllProjects", "project");
        this.listOfProjects.setValue(projects);
    }

}
