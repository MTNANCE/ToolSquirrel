package no.purplecloud.toolsquirrel.ui.manageTools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import no.purplecloud.toolsquirrel.R;

public class NewToolFragment extends Fragment {

    private AutoCompleteTextView autoCompleteProject;
    private EditText toolName;
    private EditText toolDesc;
    private EditText toolLocation;
    private Button imageUploadBtn; // TODO Just a reminder to add image upload support
    private Button submitBtn;

    // Change this to be of type Project
    private String selectedProject;

    private String[] dummy = {
            "Stadia (#1031548)",
            "Vertex (#1341323)"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_tool, container, false);
        this.autoCompleteProject = rootView.findViewById(R.id.add_tool_project_selector);
        this.toolName = rootView.findViewById(R.id.new_tool_name);
        this.toolDesc = rootView.findViewById(R.id.new_tool_desc);
        this.toolLocation = rootView.findViewById(R.id.new_tool_location);
        this.submitBtn = rootView.findViewById(R.id.add_tool_button);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, dummy);
        // Define the threshold point where it is going to start searching
        this.autoCompleteProject.setThreshold(1);
        // Attach adapter
        this.autoCompleteProject.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO Add listeners
    }
}
