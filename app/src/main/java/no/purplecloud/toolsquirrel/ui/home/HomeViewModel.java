package no.purplecloud.toolsquirrel.ui.home;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import no.purplecloud.toolsquirrel.Endpoints;
import no.purplecloud.toolsquirrel.domain.Tool;
import no.purplecloud.toolsquirrel.network.VolleySingleton;

public class HomeViewModel extends AndroidViewModel {

    // Observable list of tools
    private MutableLiveData<List<Tool>> listOfTools;
    // Observable selected tool
    private MutableLiveData<Tool> selectedTool = new MutableLiveData<>();

    private Context context;

    public HomeViewModel(Application context) {
        super(context);
        this.context = context;
    }

    public void setSelectedTool(Tool selectedTool) {
        this.selectedTool.setValue(selectedTool);
    }

    public MutableLiveData<Tool> getSelectedTool() {
        return this.selectedTool;
    }

    public LiveData<List<Tool>> getTools() {
        if (this.listOfTools == null) {
            this.listOfTools = new MutableLiveData<>();
            getAllTools();
        }
        return this.listOfTools;
    }

    public void searchForTools(String search) {
        VolleySingleton.getInstance(this.context)
                .searchPostRequest(Endpoints.URL + "/searchTool/", search, "tool", listOfTools::setValue);

    }

    private void getAllTools() {
        VolleySingleton.getInstance(this.context)
                .getListRequest(Endpoints.URL + "/getAllTools", "tool", listOfTools::setValue);
    }

    private void getAllUniqueTools() {
        VolleySingleton.getInstance(this.context)
                .getListRequest(Endpoints.URL + "/getAllUniqueTools", "tool", listOfTools::setValue);
    }

}
