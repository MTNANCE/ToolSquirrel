package no.purplecloud.toolsquirrel.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import no.purplecloud.toolsquirrel.Endpoints;
import no.purplecloud.toolsquirrel.R;
import no.purplecloud.toolsquirrel.domain.Tool;
import no.purplecloud.toolsquirrel.domain.ToolStatus;
import no.purplecloud.toolsquirrel.network.VolleySingleton;
import no.purplecloud.toolsquirrel.singleton.CacheSingleton;

public class ToolDetailsFragment extends Fragment {

    private ImageView image;
    private TextView title;
    private TextView description;
    private TextView txtTotalAvailable;
    private TableLayout tableLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeViewModel homeViewModel = ViewModelProviders.of(this.getActivity()).get(HomeViewModel.class);
        homeViewModel.getSelectedTool().observe(this, tool -> {
            Picasso.get().load(tool.getToolImage()).into(this.image);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(tool.getToolName());
            this.title.setText(tool.getToolName());
            this.description.setText(tool.getToolDesc());
            getAllDuplicateTools(tool.getToolName());
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_tool_details, container, false);
        this.image = result.findViewById(R.id.tool_details_image);
        this.title = result.findViewById(R.id.tool_details_tile);
        this.description = result.findViewById(R.id.tool_details_desc);
        this.txtTotalAvailable = result.findViewById(R.id.tool_details_availability);
        this.tableLayout = result.findViewById(R.id.tool_availability_table);
        return result;
    }

    private void getAllDuplicateTools(String toolName) {
        try {
            int projectId = new JSONObject(CacheSingleton.getInstance(getContext()).loadFromData("activeProject")).getInt("id");
            System.out.println(Endpoints.URL + "/tools/status/".concat(String.valueOf(projectId).concat("/")));
            VolleySingleton.getInstance(getContext())
                    .searchGetRequest(Endpoints.URL + "/tools/status/".concat(String.valueOf(projectId).concat("/")), toolName, "toolstatus",
                            this::formatToolAvailabilityTable);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    private void formatToolAvailabilityTable(List<ToolStatus> toolStatuses) {
        AtomicInteger totalAvailable = new AtomicInteger(0);

        toolStatuses.forEach(status -> {
            TableRow tr = new TableRow(getContext());

            TextView txtId = new TextView(getContext());
            TextView txtStatus = new TextView(getContext());
            TextView txtLocation = new TextView(getContext());

            txtId.setText("#".concat(String.valueOf(status.getId())));
            txtStatus.setText((status.isAvailable()) ? "Available" : "Loaned");
            txtLocation.setText(status.getLocation());

            List<TextView> textViews = Arrays.asList(txtId, txtStatus, txtLocation);

            textViews.forEach(textView -> {
                textView.setTextSize(14);
                tr.addView(textView);
            });

            tableLayout.addView(tr);

            // Check if the tool is available or not and add it to the counters
            if (status.isAvailable()) {
                totalAvailable.set(totalAvailable.get() + 1);
            }
        });

        String availableText = toolStatuses.size() + " tools, " + totalAvailable.get() + " available";
        txtTotalAvailable.setText(availableText);
    }
}
