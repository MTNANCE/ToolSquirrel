package no.purplecloud.toolsquirrel.ui.projectLeaders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import no.purplecloud.toolsquirrel.R;
import no.purplecloud.toolsquirrel.domain.Employee;

public class ProjectLeadersRecyclerAdapter extends RecyclerView.Adapter<ProjectLeadersRecyclerAdapter.ViewHolder> {

    private ProjectLeadersViewModel projectLeadersViewModel;

    private final List<Employee> listOfProjectLeaders;

    public ProjectLeadersRecyclerAdapter(List<Employee> listOfProjectLeaders) {
        this.listOfProjectLeaders = listOfProjectLeaders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_project_leader_item, parent, false);
        ProjectLeadersRecyclerAdapter.ViewHolder viewHolder = new ProjectLeadersRecyclerAdapter.ViewHolder(view);
        this.projectLeadersViewModel = ViewModelProviders.of((FragmentActivity) parent.getContext()).get(ProjectLeadersViewModel.class);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(this.listOfProjectLeaders.get(position).getImage()).into(holder.image);
        holder.name.setText(this.listOfProjectLeaders.get(position).getName());
        holder.position.setText(this.listOfProjectLeaders.get(position).getPosition());
        holder.phone.setText(String.valueOf(this.listOfProjectLeaders.get(position).getPhone()));
        holder.view.setOnClickListener(v -> projectLeadersViewModel.setSelectedLeader(this.listOfProjectLeaders.get(position)));
    }

    @Override
    public int getItemCount() {
        return this.listOfProjectLeaders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        ImageView image;
        TextView name;
        TextView position;
        TextView phone;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.view = view;
            this.image = view.findViewById(R.id.project_leader_image);
            this.name = view.findViewById(R.id.project_leader_name);
            this.position = view.findViewById(R.id.project_leader_position);
            this.phone = view.findViewById(R.id.project_leader_phone);
        }

    }

}
