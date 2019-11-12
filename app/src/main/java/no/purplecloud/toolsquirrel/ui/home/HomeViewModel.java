package no.purplecloud.toolsquirrel.ui.home;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

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
        String url = "http://192.168.1.97:8080/searchTool/" + search;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, null,
            response -> {
                System.out.println("Response: " + response);
                List<Tool> tools = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        tools.add(new Tool(response.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                this.listOfTools.setValue(tools);
            }, error -> {
                System.out.println("ERROR: " + error);
            }
        );
        VolleySingleton.getInstance(this.context).addToRequestQueue(jsonArrayRequest);
    }

    private void getAllTools() {
        String url = "http://192.168.1.97:8080/getAllTools";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
            response -> {
                System.out.println("Response: " + response);
                List<Tool> tools = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            tools.add(new Tool(response.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Tools: " + tools);
                this.listOfTools.setValue(tools);
            },
            error -> {
                System.out.println("ERROR: " + error);
            }
        );
        VolleySingleton.getInstance(this.context).addToRequestQueue(jsonArrayRequest);
    }

}
