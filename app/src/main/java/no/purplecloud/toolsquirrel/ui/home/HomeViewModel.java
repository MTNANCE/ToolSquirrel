package no.purplecloud.toolsquirrel.ui.home;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
        List<Tool> tools = VolleySingleton.getInstance(this.context)
                .searchPostRequest("http://192.168.1.97:8080/searchTool/", search, "tool");
        this.listOfTools.setValue(tools);
    }

    private void getAllTools() {
        List<Tool> tools = VolleySingleton.getInstance(this.context)
                .getListRequest("http://192.168.1.97:8080/getAllTools", "tool");
        this.listOfTools.setValue(tools);
    }

}
