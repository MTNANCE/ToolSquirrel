package no.purplecloud.toolsquirrel.ui.manageEmployees;

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

public class ManageEmployeesRecyclerAdapter extends RecyclerView.Adapter<ManageEmployeesRecyclerAdapter.ViewHolder> {

    private ManageEmployeesViewModel manageEmployeesViewModel;

    private final List<Employee> listOfEmployees;

    public ManageEmployeesRecyclerAdapter(List<Employee> listOfEmployees) {
        this.listOfEmployees = listOfEmployees;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_project_leader_item, parent, false);
        ManageEmployeesRecyclerAdapter.ViewHolder viewHolder = new ManageEmployeesRecyclerAdapter.ViewHolder(view);
        this.manageEmployeesViewModel = ViewModelProviders.of((FragmentActivity) parent.getContext()).get(ManageEmployeesViewModel.class);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(this.listOfEmployees.get(position).getImage()).into(holder.image);
        holder.name.setText(this.listOfEmployees.get(position).getName());
        holder.position.setText(this.listOfEmployees.get(position).getPosition());
        holder.phone.setText("Tlf..: " + this.listOfEmployees.get(position).getPhone());
        holder.view.setOnClickListener(v -> this.manageEmployeesViewModel.setSelectedEmployee(this.listOfEmployees.get(position)));
    }

    @Override
    public int getItemCount() {
        return this.listOfEmployees.size();
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
