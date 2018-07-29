package com.portfolio.jgsilveira.customersportfolio.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.portfolio.jgsilveira.customersportfolio.R;
import com.portfolio.jgsilveira.customersportfolio.dao.CustomerDao;
import com.portfolio.jgsilveira.customersportfolio.model.Customer;
import com.portfolio.jgsilveira.customersportfolio.model.ReportFilters;
import com.portfolio.jgsilveira.customersportfolio.settings.EnumStates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReportViewModel extends AppViewModel {

    private ReportFilters mFilters = new ReportFilters();

    private MutableLiveData<Boolean> mHasResultado;

    private CustomerDao mDao;

    private List<Customer> mResults = new ArrayList<>();

    private List<EnumStates> mStates = new ArrayList<>();

    private int mStatesPosition = 0;

    public ReportViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    @Override
    protected void onCleared() {
        if (hasAnyTaskRunning()) {
            mTask.cancel(true);
        }
        super.onCleared();
    }

    private void init() {
        mDao = getDatabase().clienteDao();
        loadStates();
    }

    private void loadStates() {
        List<EnumStates> states = Arrays.asList(EnumStates.values());
        mStates.clear();
        mStates.addAll(states);
    }

    public MutableLiveData<Boolean> getHasResultado() {
        if (mHasResultado == null) {
            mHasResultado = new MutableLiveData<>();
            mHasResultado.setValue(false);
        }
        return mHasResultado;
    }

    public List<Customer> getResults() {
        return mResults;
    }

    public void generateReport() {
        AsyncReportTask task = new AsyncReportTask();
        mTask = task;
        task.execute();
    }

    @NonNull
    public ReportFilters getFilters() {
        return mFilters;
    }

    public void selectState(int position) {
        mStatesPosition = position;
        EnumStates state = mStates.get(position);
        mFilters.setState(state.getSigla());
    }

    public int getStateSelectedPosition() {
        return mStatesPosition;
    }

    public List<EnumStates> getStates() {
        return mStates;
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncReportTask extends AsyncTask<Void, Void, List<Customer>> {

        @Override
        protected List<Customer> doInBackground(Void... voids) {
            return mDao.queryReport(mFilters.getName(), mFilters.getBornedFrom(),
                    mFilters.getBornedTo(), mFilters.getStartDate(), mFilters.getEndDate(),
                    mFilters.getDocument(), mFilters.getState());
        }

        @Override
        protected void onPreExecute() {
            mProcessing.setValue(true);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Customer> clientes) {
            mProcessing.setValue(false);
            boolean withoutResult = clientes.isEmpty();
            if (withoutResult) {
                mHasResultado.setValue(false);
                mMessage.setValue(getString(R.string.nenhum_resultado_encontrado));
            } else {
                mHasResultado.setValue(true);
                mResults.clear();
                mResults.addAll(clientes);
            }
            super.onPostExecute(clientes);
        }

        @Override
        protected void onCancelled() {
            mProcessing.setValue(false);
            super.onCancelled();
        }

    }

}
