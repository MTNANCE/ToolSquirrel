package no.purplecloud.toolsquirrel.ui.loan;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import no.purplecloud.toolsquirrel.Endpoints;
import no.purplecloud.toolsquirrel.domain.Employee;
import no.purplecloud.toolsquirrel.domain.Loan;
import no.purplecloud.toolsquirrel.network.VolleySingleton;
import no.purplecloud.toolsquirrel.singleton.CacheSingleton;

public class LoansViewModel extends AndroidViewModel {

    // Observable list of loans
    private MutableLiveData<List<Loan>> listOfLoans;

    private Context context;

    public LoansViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
    }

    public LiveData<List<Loan>> getLoans() {
        if (this.listOfLoans == null) {
            this.listOfLoans = new MutableLiveData<>();
            getListOfLoans();
        }
        return this.listOfLoans;
    }

    public void getListOfLoans() {
        Employee employee = CacheSingleton.getInstance(context).getAuthenticatedUser();
        VolleySingleton.getInstance(context).getListRequest(Endpoints.URL + "/findAllBorrows/" + employee.getId(), "loan",
                list -> listOfLoans.setValue(list));
    }

    public void setListOfLoans(List<Loan> list) {
        this.listOfLoans.setValue(list);
    }
}
