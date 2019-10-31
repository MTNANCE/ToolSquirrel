package no.purplecloud.toolsquirrel.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import no.purplecloud.toolsquirrel.domain.Tool;

public class HomeViewModel extends ViewModel {

    // Observable list of tools
    private MutableLiveData<List<Tool>> listOfTools;
    // Observable selected tool
    private MutableLiveData<Tool> selectedTool = new MutableLiveData<>();

    private List<Tool> listOfDummyTools;

    public HomeViewModel() {
        this.listOfDummyTools = new ArrayList<>();
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

    public void getAllTools() {
        try {
            HttpResponse<String> response = Unirest.get("http://10.0.2.2:8080/getAllTools").asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    private void initDummyData() {
        this.listOfDummyTools.add(new Tool(1L, "https://cdn.bmstores.co.uk/images/hpcProductImage/imgFull/337811-6-way-screwdriver.jpg", "Skutrekker", "Skrutrekker fra Jula", "L123"));
        this.listOfDummyTools.add(new Tool(2L, "https://www.dhresource.com/0x0s/f2-albu-g7-M01-DB-39-rBVaSVsR94WAZh2-AAVGzLYwqIM570.jpg/yikoda-21v-electric-screwdriver-cordless.jpg", "Skrumaskin", "Mx154", "L122"));
        this.listOfDummyTools.add(new Tool(3L, "https://images-na.ssl-images-amazon.com/images/I/81zEGnZTe1L._SX425_.jpg", "Vinkelsliper", "x330", "A100"));
        this.listOfDummyTools.add(new Tool(4L, "https://images-na.ssl-images-amazon.com/images/I/61OPU27mdSL._SX425_.jpg", "Drill", "Drill fra Jula", "A103"));
        this.listOfDummyTools.add(new Tool(5L, "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSTjz00sUmF0uJ5CAbDW9bDYQwv6xSO3Arg61x3wVdgTj4XKysw", "Minarc Sveiseapparat", "180 EVO", "A100"));
        this.listOfTools.setValue(this.listOfDummyTools);
    }
}
