package no.purplecloud.toolsquirrel.ui.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import no.purplecloud.toolsquirrel.R;
import no.purplecloud.toolsquirrel.domain.Tool;

/**
 * Recycler view adapter for the list of tools
 */
public class ToolListRecyclerViewAdapter extends RecyclerView.Adapter<ToolListRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "ToolListRecyclerViewAdapter";

    private HomeViewModel homeViewModel;

    // TODO This is currently a list of dummy tools
    private final List<Tool> listOfDummyTools;

    public ToolListRecyclerViewAdapter(List<Tool> listOfTools) {
        this.listOfDummyTools = listOfTools;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        this.homeViewModel = ViewModelProviders.of((FragmentActivity) parent.getContext()).get(HomeViewModel.class);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        // Is called for every widget that is added (DEBUG purposes
        Log.d(TAG, "onBindViewHolder: called.");

        Picasso.get().load(this.listOfDummyTools.get(position).getToolImage()).into(viewHolder.image);
        viewHolder.toolTitle.setText(this.listOfDummyTools.get(position).getToolName());
        viewHolder.toolDesc.setText(this.listOfDummyTools.get(position).getToolDesc());
        viewHolder.toolInfo.setText(this.listOfDummyTools.get(position).getToolLocation());
        viewHolder.view.setOnClickListener(v -> homeViewModel.setSelectedTool(this.listOfDummyTools.get(position)));
    }

    @Override
    public int getItemCount() {
        return this.listOfDummyTools.size();
    }

    /**
     * Will hold each widget/view in memory
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        ImageView image;
        TextView toolTitle;
        TextView toolDesc;
        TextView toolInfo;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            this.image = itemView.findViewById(R.id.list_item_image);
            this.toolTitle = itemView.findViewById(R.id.list_item_tool_title);
            this.toolDesc = itemView.findViewById(R.id.list_item_tool_desc);
            this.toolInfo = itemView.findViewById(R.id.list_item_tool_info);
            this.parentLayout = itemView.findViewById(R.id.list_item_parent_layout);
        }
    }

}
