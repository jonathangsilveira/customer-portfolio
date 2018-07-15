package com.portfolio.jgsilveira.customersportfolio.database;

import android.os.AsyncTask;

import com.portfolio.jgsilveira.customersportfolio.util.StringUtil;

public class AsyncDatabaseTransactionTask extends AsyncTask<Object, Void, Boolean> {

    private TransactionCallback mCallback;

    @Override
    protected void onPreExecute() {
        if (hasCallback()) {
            mCallback.onPreTransaction();
        }
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Object... args) {
        if (hasCallback()) {
            return mCallback.onTransaction();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (hasCallback()) {
            mCallback.onPostTransaction(success, StringUtil.VAZIO);
        }
        super.onPostExecute(success);
    }

    @Override
    protected void onCancelled() {
        if (hasCallback()) {
            mCallback.onCancelTransaction();
        }
        super.onCancelled();
    }

    public void registerCallback(TransactionCallback callback) {
        this.mCallback = callback;
    }

    private boolean hasCallback() {
        return mCallback != null;
    }

    public interface TransactionCallback {

        void onPreTransaction();

        boolean onTransaction();

        void onPostTransaction(boolean success, String message);

        void onCancelTransaction();

    }

}
