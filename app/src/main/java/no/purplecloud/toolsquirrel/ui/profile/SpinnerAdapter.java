package no.purplecloud.toolsquirrel.ui.profile;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import no.purplecloud.toolsquirrel.domain.Project;

public class SpinnerAdapter extends ArrayAdapter<Project> {

    private Context context;

    private List<Project> listOfProjects;

    public SpinnerAdapter(@NonNull Context context, int resource, List<Project> listOfProjects) {
        super(context, resource);
        this.context = context;
        this.listOfProjects = listOfProjects;
    }

    @Override
    public int getCount() {
        return this.listOfProjects.size();
    }

    @Nullable
    @Override
    public Project getItem(int position) {
        return this.listOfProjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.listOfProjects.get(position).getProjectId();
    }

    /**
     * Passive state
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.parseColor("#8E8E8E"));
        label.setText(this.listOfProjects.get(position).getProjectName() + " (#" + this.listOfProjects.get(position).getProjectId() + ")");
        return label;
    }

    /**
     * Active state (popped)
     */
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.parseColor("#8E8E8E"));
        label.setText(this.listOfProjects.get(position).getProjectName() + " (#" + this.listOfProjects.get(position).getProjectId() + ")");
        return label;
    }
}
