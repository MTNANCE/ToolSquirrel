package no.purplecloud.toolsquirrel.ui.project;

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
import no.purplecloud.toolsquirrel.domain.Project;
import no.purplecloud.toolsquirrel.domain.Tool;
import no.purplecloud.toolsquirrel.ui.home.HomeViewModel;
import no.purplecloud.toolsquirrel.ui.home.ToolListRecyclerViewAdapter;

public class ProjectListRecyclerAdapter extends RecyclerView.Adapter<ProjectListRecyclerAdapter.ViewHolder> {

    private static final String TAG = "ProjectListRecyclerAdapter";

    private ProjectViewModel projectViewModel;

    private final List<Project> listOfProjects;

    public ProjectListRecyclerAdapter(List<Project> listOfProjects) {
        this.listOfProjects = listOfProjects;
    }

    @NonNull
    @Override
    public ProjectListRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_project_item, parent, false);
        ProjectListRecyclerAdapter.ViewHolder viewHolder = new ProjectListRecyclerAdapter.ViewHolder(view);
        this.projectViewModel = ViewModelProviders.of((FragmentActivity) parent.getContext()).get(ProjectViewModel.class);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectListRecyclerAdapter.ViewHolder viewHolder, int position) {
        // Is called for every widget that is added (DEBUG purposes
        Log.d(TAG, "onBindViewHolder: called.");
        Picasso.get().load(this.listOfProjects.get(position).getProjectImage()).into(viewHolder.image);
        viewHolder.projectTitle.setText(this.listOfProjects.get(position).getProjectName());
        viewHolder.projectDesc.setText(this.listOfProjects.get(position).getProjectDescription());
        viewHolder.view.setOnClickListener(v -> projectViewModel.setSelectedProject(this.listOfProjects.get(position)));
    }

    @Override
    public int getItemCount() {
        return this.listOfProjects.size();
    }

    /**
     * Will hold each widget/view in memory
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        ImageView image;
        TextView projectTitle;
        TextView projectDesc;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            this.image = itemView.findViewById(R.id.project_item_image);
            this.projectTitle = itemView.findViewById(R.id.project_item_title);
            this.projectDesc = itemView.findViewById(R.id.project_item_desc);
            this.parentLayout = itemView.findViewById(R.id.project_item_parent_layout);
        }
    }

}
