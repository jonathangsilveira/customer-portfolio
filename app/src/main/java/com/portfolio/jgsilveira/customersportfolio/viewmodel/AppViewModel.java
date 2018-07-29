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

    MutableLiveData<String> mErrorMessage;

    MutableLiveData<String> mMessage;

    MutableLiveData<Boolean> mProcessing = new MutableLiveData<>();

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

    public LiveData<Boolean> getProcessing() {
        return mProcessing;
    }

    public LiveData<String> getMessage() {
        if (mMessage == null) {
            mMessage = new MutableLiveData<>();
            mMessage.setValue(StringUtil.VAZIO);
        }
        return mMessage;
    }

    public LiveData<String> getErrorMessage() {
        if (mErrorMessage == null) {
            mErrorMessage = new MutableLiveData<>();
            mErrorMessage.setValue(StringUtil.VAZIO);
        }
        return mErrorMessage;
    }

    boolean hasAnyTaskRunning() {
        return mTask != null && !mTask.isCancelled();
    }

}
