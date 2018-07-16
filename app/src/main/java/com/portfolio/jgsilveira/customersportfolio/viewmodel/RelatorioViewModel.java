package com.portfolio.jgsilveira.customersportfolio.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.portfolio.jgsilveira.customersportfolio.R;
import com.portfolio.jgsilveira.customersportfolio.dao.ClienteDao;
import com.portfolio.jgsilveira.customersportfolio.database.AsyncDatabaseTransactionTask;
import com.portfolio.jgsilveira.customersportfolio.model.Cliente;
import com.portfolio.jgsilveira.customersportfolio.model.FiltroRelatorio;

import java.util.ArrayList;
import java.util.List;

public class RelatorioViewModel extends AppViewModel {

    private MutableLiveData<FiltroRelatorio> mFiltro;

    private MutableLiveData<List<Cliente>> mResultado;

    private ClienteDao mDao;

    private AsyncDatabaseTransactionTask mTask;

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
        mTask = new AsyncDatabaseTransactionTask();
        mTask.registerCallback(new DatabaseQuery());
        mTask.execute();
    }

    private boolean hasAnyTaskRunning() {
        return mTask != null && !mTask.isCancelled();
    }

    private class DatabaseQuery implements AsyncDatabaseTransactionTask.TransactionCallback {

        private List<Cliente> mClientes;

        @Override
        public void onPreTransaction() {
            mProcessando.setValue(true);
        }

        @Override
        public boolean onTransaction() {
            FiltroRelatorio filtro = mFiltro.getValue();
            if (filtro != null) {
                mClientes = mDao.queryWithParameters(filtro.getNome(), filtro.getDataNascimento(),
                        filtro.getDataInicio(), filtro.getDataFim());
                return !mClientes.isEmpty();
            }
            return false;
        }

        @Override
        public void onPostTransaction(boolean success, String message) {
            mProcessando.setValue(false);
            if (success) {
                mResultado.setValue(mClientes);
            } else {
                mMensagem.setValue(getString(R.string.nenhum_resultado_encontrado));
            }
        }

        @Override
        public void onCancelTransaction() {
            mProcessando.setValue(false);
        }

    }

}
