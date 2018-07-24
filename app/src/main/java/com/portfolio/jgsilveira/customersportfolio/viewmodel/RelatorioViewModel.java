package com.portfolio.jgsilveira.customersportfolio.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.portfolio.jgsilveira.customersportfolio.R;
import com.portfolio.jgsilveira.customersportfolio.dao.ClienteDao;
import com.portfolio.jgsilveira.customersportfolio.model.Cliente;
import com.portfolio.jgsilveira.customersportfolio.model.FiltroRelatorio;

import java.util.ArrayList;
import java.util.List;

public class RelatorioViewModel extends AppViewModel {

    private MutableLiveData<FiltroRelatorio> mFiltro;

    private MutableLiveData<List<Cliente>> mResultado;

    private ClienteDao mDao;

    public RelatorioViewModel(@NonNull Application application) {
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
        getFiltro();
    }

    public LiveData<FiltroRelatorio> getFiltro() {
        if (mFiltro == null) {
            mFiltro = new MutableLiveData<>();
            mFiltro.setValue(new FiltroRelatorio());
        }
        return mFiltro;
    }

    public LiveData<List<Cliente>> getResultado() {
        if (mResultado == null) {
            mResultado = new MutableLiveData<>();
            mResultado.setValue(new ArrayList<Cliente>());
        }
        return mResultado;
    }

    public void gerarRelatorio() {
        AsyncReportTask task = new AsyncReportTask();
        mTask = task;
        task.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncReportTask extends AsyncTask<Void, Void, List<Cliente>> {

        @Override
        protected List<Cliente> doInBackground(Void... voids) {
            FiltroRelatorio filtro = mFiltro.getValue();
            List<Cliente> clientes = new ArrayList<>();
            if (filtro != null) {
                clientes.addAll(mDao.queryWithParameters(filtro.getNome(),
                        filtro.getDataNascimento(), filtro.getDataInicio(), filtro.getDataFim()));
            }
            return clientes;
        }

        @Override
        protected void onPreExecute() {
            mProcessando.setValue(true);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Cliente> clientes) {
            mProcessando.setValue(false);
            boolean withoutResult = clientes.isEmpty();
            if (withoutResult) {
                mMensagem.setValue(getString(R.string.nenhum_resultado_encontrado));
            } else {
                mResultado.setValue(clientes);
            }
            super.onPostExecute(clientes);
        }

        @Override
        protected void onCancelled() {
            mProcessando.setValue(false);
            super.onCancelled();
        }

    }

}
