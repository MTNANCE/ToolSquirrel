package no.purplecloud.toolsquirrel.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.squareup.picasso.Picasso;

import no.purplecloud.toolsquirrel.R;

public class ToolDetailsFragment extends Fragment {

    private ImageView image;
    private TextView title;
    private TextView description;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeViewModel homeViewModel = ViewModelProviders.of(this.getActivity()).get(HomeViewModel.class);
        homeViewModel.getSelectedTool().observe(this, tool -> {
            Picasso.get().load(tool.getToolImage()).into(this.image);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(tool.getToolName());
            this.title.setText(tool.getToolName());
            this.description.setText(tool.getToolDesc());
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_tool_details, container, false);
        this.image = result.findViewById(R.id.tool_details_image);
        this.title = result.findViewById(R.id.tool_details_tile);
        this.description = result.findViewById(R.id.tool_details_desc);
        return result;
    }
}
