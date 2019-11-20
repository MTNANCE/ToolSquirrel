package no.purplecloud.toolsquirrel.ui.manageTools;

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

import java.util.List;

import no.purplecloud.toolsquirrel.R;
import no.purplecloud.toolsquirrel.domain.Tool;
import no.purplecloud.toolsquirrel.ui.home.HomeViewModel;
import no.purplecloud.toolsquirrel.ui.home.ToolListRecyclerViewAdapter;

public class ManageToolRecyclerAdapter extends RecyclerView.Adapter<ManageToolRecyclerAdapter.ViewHolder> {

    private ManageToolsViewModel manageToolsViewModel;

    private final List<Tool> listOfTools;

    public ManageToolRecyclerAdapter(List<Tool> listOfTools) {
        this.listOfTools = listOfTools;
    }

    @NonNull
    @Override
    public ManageToolRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_list_item, parent, false);
        ManageToolRecyclerAdapter.ViewHolder viewHolder = new ManageToolRecyclerAdapter.ViewHolder(view);
        this.manageToolsViewModel = ViewModelProviders.of((FragmentActivity) parent.getContext()).get(ManageToolsViewModel.class);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ManageToolRecyclerAdapter.ViewHolder viewHolder, int position) {
        Picasso.get().load(this.listOfTools.get(position).getToolImage()).into(viewHolder.image);
        viewHolder.toolTitle.setText(this.listOfTools.get(position).getToolName());
        viewHolder.toolDesc.setText(this.listOfTools.get(position).getToolDesc());
        viewHolder.toolId.setText("#" + String.valueOf(this.listOfTools.get(position).getId()));
        viewHolder.view.setOnClickListener(v -> manageToolsViewModel.setSelectedTool(this.listOfTools.get(position)));
    }

    @Override
    public int getItemCount() {
        return this.listOfTools.size();
    }

    /**
     * Will hold each widget/view in memory
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        ImageView image;
        TextView toolTitle;
        TextView toolDesc;
        TextView toolId;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            this.image = itemView.findViewById(R.id.list_item_image);
            this.toolTitle = itemView.findViewById(R.id.list_item_tool_title);
            this.toolDesc = itemView.findViewById(R.id.list_item_tool_desc);
            this.toolId = itemView.findViewById(R.id.list_item_tool_info);
            this.parentLayout = itemView.findViewById(R.id.list_item_parent_layout);
        }
    }

}
