package no.purplecloud.toolsquirrel.ui.manageTools;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import no.purplecloud.toolsquirrel.domain.Employee;
import no.purplecloud.toolsquirrel.domain.Tool;

public class ManageToolsViewModel extends AndroidViewModel {

    // Observable list of tools
    private MutableLiveData<List<Tool>> listOfTools;
    // Observable selected tool
    private MutableLiveData<Tool> selectedTool = new MutableLiveData<>();

    private Long selectedProject;

    private Context context;

    public ManageToolsViewModel(Application context) {
        super(context);
        this.context = context;
    }

    public void setSelectedTool(Tool tool) {
        this.selectedTool.setValue(tool);
    }

    public MutableLiveData<Tool> getSelectedTool() {
        return this.selectedTool;
    }

    public LiveData<List<Tool>> getTools() {
        if (this.listOfTools == null) {
            this.listOfTools = new MutableLiveData<>();
        }
        return this.listOfTools;
    }

    public void setListOfTools(List<Tool> list) {
        this.listOfTools.setValue(list);
    }

    public void setSelectedProject(Long selectedProject) {
        this.selectedProject = selectedProject;
    }

}
