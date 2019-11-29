package no.purplecloud.toolsquirrel.ui.home;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import no.purplecloud.toolsquirrel.Endpoints;
import no.purplecloud.toolsquirrel.domain.Tool;
import no.purplecloud.toolsquirrel.network.VolleySingleton;
import no.purplecloud.toolsquirrel.singleton.CacheSingleton;

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
            getAllUniqueTools();
        }
        return this.listOfTools;
    }

    public void searchForTools(String search) {
        try {
            // TODO Redundant?
            JSONObject activeProject = new JSONObject(CacheSingleton.getInstance(context).loadFromData("activeProject"));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("search", search);
            jsonObject.put("project_id", activeProject.getInt("id"));

            VolleySingleton.getInstance(this.context)
                    .searchPostRequestWithBody(Endpoints.URL + "/searchTool", jsonObject, "tool", listOfTools::setValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getAllUniqueTools() {
        try {
            JSONObject activeProject = new JSONObject(CacheSingleton.getInstance(context).loadFromData("activeProject"));
            VolleySingleton.getInstance(this.context)
                    .getListRequest(Endpoints.URL + "/getAllUniqueToolsByProject/" + activeProject.getInt("id"), "tool", listOfTools::setValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
      }

}
