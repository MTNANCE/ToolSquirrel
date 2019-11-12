package no.purplecloud.toolsquirrel.ui.project;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import no.purplecloud.toolsquirrel.domain.Project;

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

    private void getAllProjects() {
        ArrayList<Project> dummyProjects = new ArrayList<>();
        dummyProjects.add(new Project(1L, "Scandic Lerkendal", "Nytt hotell med fabulicious utsikt", "https://media-cdn.tripadvisor.com/media/photo-s/06/69/27/47/scandic-lerkendal.jpg"));
        dummyProjects.add(new Project(2L, "Vixxy Waxxy", "tom for ideer", "https://mondrian.mashable.com/uploads%252Fcard%252Fimage%252F829044%252Ff1a11a98-59ed-46a9-a2df-bf2a6997ee31.jpg%252F950x534__filters%253Aquality%252880%2529.jpg?signature=e4NQ844Xd_xOQoflFnhXVABg_AA=&source=https%3A%2F%2Fblueprint-api-production.s3.amazonaws.com"));
        this.listOfProjects.setValue(dummyProjects);
    }

}
