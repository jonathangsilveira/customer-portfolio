package com.portfolio.jgsilveira.customersportfolio.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.portfolio.jgsilveira.customersportfolio.database.AppDatabase;
import com.portfolio.jgsilveira.customersportfolio.database.AppDatabaseFactory;
import com.portfolio.jgsilveira.customersportfolio.util.StringUtil;

public abstract class AppViewModel extends AndroidViewModel {

    private AppDatabaseFactory mDatabase;

    MutableLiveData<String> mMensagemErro;

    MutableLiveData<String> mMensagem;

    MutableLiveData<Boolean> mProcessando = new MutableLiveData<>();

    AsyncTask mTask;

    AppViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    private void init() {
        mDatabase = AppDatabase.getInstance(getApplication().getApplicationContext());
    }

    AppDatabaseFactory getDatabase() {
        return mDatabase;
    }

    String getString(@StringRes int resId) {
        return getApplication().getString(resId);
    }

    String getString(@StringRes int resId, Object... args) {
        return getApplication().getString(resId, args);
    }

    public LiveData<Boolean> getProcessando() {
        return mProcessando;
    }

    public LiveData<String> getMensagem() {
        if (mMensagem == null) {
            mMensagem = new MutableLiveData<>();
            mMensagem.setValue(StringUtil.VAZIO);
        }
        return mMensagem;
    }

    public LiveData<String> getMensagemErro() {
        if (mMensagemErro == null) {
            mMensagemErro = new MutableLiveData<>();
            mMensagemErro.setValue(StringUtil.VAZIO);
        }
        return mMensagemErro;
    }

    boolean hasAnyTaskRunning() {
        return mTask != null && !mTask.isCancelled();
    }

}
