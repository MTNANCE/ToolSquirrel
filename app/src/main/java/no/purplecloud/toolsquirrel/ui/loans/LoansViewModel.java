package no.purplecloud.toolsquirrel.ui.loans;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import no.purplecloud.toolsquirrel.domain.Loan;

public class LoansViewModel extends AndroidViewModel {

    // Observable list of loans
    private MutableLiveData<List<Loan>> listOfLoans;

    private Context context;

    private ArrayList<Loan> dummyLoans = new ArrayList<>();

    public LoansViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
    }

    public LiveData<List<Loan>> getLoans() {
        if (this.listOfLoans == null) {
            this.listOfLoans = new MutableLiveData<>();
            fillWithDummyLoans();
        }
        return this.listOfLoans;
    }

    //  TODO Implement this method (API call)
    private void getAllLoans() {

    }

    // TESTING (TODO Remove after implementing method above)
    private void fillWithDummyLoans() {
        this.dummyLoans.add(new Loan(12345678L, "Test loan space", "04/11/2019"));
        this.dummyLoans.add(new Loan(45216487L, "Dummy shit", "15/10/2019"));
        this.dummyLoans.add(new Loan(91235484L, "Svær banan", "07/11/2019"));
        this.listOfLoans.setValue(this.dummyLoans);
    }
}
