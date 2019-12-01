package no.purplecloud.toolsquirrel.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import no.purplecloud.toolsquirrel.domain.Employee;
import no.purplecloud.toolsquirrel.domain.Project;

public class EmployeeAdapter extends ArrayAdapter<Employee> {

    private Context context;

    private List<Employee> employeeList;

    public EmployeeAdapter(@NonNull Context context, int resource, List<Employee> employeeList) {
        super(context, resource);
        this.context = context;
        this.employeeList = employeeList;
    }

    @Override
    public int getCount() {
        return this.employeeList.size();
    }

    @Nullable
    @Override
    public Employee getItem(int position) {
        return this.employeeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.employeeList.get(position).getId();
    }

    /**
     * Passive state
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.parseColor("#8E8E8E"));
        label.setText(this.employeeList.get(position).getName() + " (#" + this.employeeList.get(position).getId() + ")");
        return label;
    }

    /**
     * Active state (popped)
     */
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.parseColor("#8E8E8E"));
        label.setText(this.employeeList.get(position).getName() + " (#" + this.employeeList.get(position).getId() + ")");
        return label;
    }

}
