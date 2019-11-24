package no.purplecloud.toolsquirrel.ui.loan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import no.purplecloud.toolsquirrel.R;
import no.purplecloud.toolsquirrel.domain.Loan;

public class LoanListRecyclerViewAdapter extends RecyclerView.Adapter<LoanListRecyclerViewAdapter.ViewHolder> {

    private final List<Loan> listOfLoans;

    public LoanListRecyclerViewAdapter(List<Loan> listOfLoans) {
        this.listOfLoans = listOfLoans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_loan_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.toolTitle.setText(this.listOfLoans.get(position).getToolName());
        viewHolder.dueDate.setText(this.listOfLoans.get(position).getDueDate());
    }

    @Override
    public int getItemCount() {
        return this.listOfLoans.size();
    }

    /**
     * Holds each widget/view in memory
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView toolTitle;
        TextView dueDate;
        Button extendBtn;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.view = view;
            this.toolTitle = view.findViewById(R.id.loan_item_title);
            this.dueDate = view.findViewById(R.id.loan_item_due_date);
            this.extendBtn = view.findViewById(R.id.loan_item_button);
        }
    }
}
