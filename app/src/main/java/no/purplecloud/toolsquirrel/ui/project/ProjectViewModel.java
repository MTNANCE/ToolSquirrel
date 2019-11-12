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

    // Observable list of tools
    private MutableLiveData<List<Project>> listOfProjects;
    // Observable selected tool
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
        String url = "http://192.168.1.97:8080/searchProject/" + search;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, null,
                response -> {
                    System.out.println("Response: " + response);
                    List<Project> projects = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            projects.add(new Project(response.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    this.listOfProjects.setValue(projects);
                }, error -> {
            System.out.println("ERROR: " + error);
        }
        );
        VolleySingleton.getInstance(this.context).addToRequestQueue(jsonArrayRequest);
    }

    private void getAllProjects() {
        String url = "http://192.168.1.97:8080/findAllProjects";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    System.out.println("Response: " + response);
                    List<Project> projects = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            projects.add(new Project(response.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Tools: " + projects);
                    this.listOfProjects.setValue(projects);
                },
                error -> {
                    System.out.println("ERROR: " + error);
                }
        );
        VolleySingleton.getInstance(this.context).addToRequestQueue(jsonArrayRequest);
    }

}
