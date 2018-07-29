package com.portfolio.jgsilveira.customersportfolio.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.portfolio.jgsilveira.customersportfolio.R;
import com.portfolio.jgsilveira.customersportfolio.dao.CustomerDao;
import com.portfolio.jgsilveira.customersportfolio.model.Customer;
import com.portfolio.jgsilveira.customersportfolio.settings.AppSettings;
import com.portfolio.jgsilveira.customersportfolio.settings.EnumStates;
import com.portfolio.jgsilveira.customersportfolio.util.DateUtil;
import com.portfolio.jgsilveira.customersportfolio.util.StringUtil;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.Callable;

public class ClienteViewModel extends AppViewModel {

    private CustomerDao mDao;

    private MutableLiveData<Customer> mCliente;

    private long mId;

    private MutableLiveData<Boolean> mEditando = new MutableLiveData<>();

    public ClienteViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    private void init() {
        mDao = getDatabase().clienteDao();
    }

    public LiveData<Customer> getCliente(long id) {
        if (mCliente == null) {
            mCliente = new MutableLiveData<>();
            if (id > 0) {
                atualizarCliente(id);
            } else {
                novoCliente();
            }
        }
        return mCliente;
    }

    public void novoCliente() {
        mEditando.setValue(true);
        mCliente.postValue(new Customer());
        mId = 0;
    }

    public boolean isEditando() {
        return mId > 0;
    }

    public MutableLiveData<Boolean> getEditando() {
        return mEditando;
    }

    public void cancelarAlteracoes() {
        mEditando.setValue(false);
        Customer cliente = mCliente.getValue();
        mCliente.setValue(cliente);
    }

    private void atualizarCliente(long id) {
        mId = id;
        mEditando.setValue(false);
        buscarCliente();
    }

    private void buscarCliente() {
        AsyncQueryTesk task = new AsyncQueryTesk();
        this.mTask = task;
        task.execute(mId);
    }

    private boolean isNotAdulto(Date dataNascimento) {
        if (dataNascimento == null) {
            return true;
        }
        int idade = DateUtil.calculateAge(dataNascimento, new Date());
        return idade < 18;
    }

    private void inserir(final Customer cliente) {
        Objects.requireNonNull(cliente).setRegisterDate(new Date());
        String uf = AppSettings.getState(EnumStates.SANTA_CATARINA.getSigla());
        Objects.requireNonNull(cliente).setState(uf);
        mId = getDatabase().runInTransaction(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return mDao.insert(cliente);
            }
        });
    }

    private void atualizar(final Customer cliente) {
        getDatabase().runInTransaction(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return mDao.update(cliente);
            }
        });
    }

    public void gravar(@NonNull Customer cliente) {
        atualizarValores(cliente);
        AsyncTransactionTask task = new AsyncTransactionTask();
        mTask = task;
        task.execute();
    }

    private void atualizarValores(@NonNull Customer cliente) {
        Customer valor = mCliente.getValue();
        if (valor != null) {
            valor.setName(cliente.getName());
            valor.setDocument(cliente.getDocument());
            valor.setBirthdate(cliente.getBirthdate());
            valor.setDocumentoId(cliente.getDocumentoId());
            valor.setTelephone(cliente.getTelephone());
        }
    }

    private void gravarAssincrono() {
        Customer cliente = mCliente.getValue();
        boolean inserir = Objects.requireNonNull(cliente).getId() == 0;
        if (inserir) {
            inserir(cliente);
        } else {
            atualizar(cliente);
        }
    }

    private boolean isCatarinense() {
        Customer cliente = mCliente.getValue();
        if (cliente == null) {
            return false;
        }
        String uf;
        if (TextUtils.isEmpty(cliente.getState())) {
            uf = AppSettings.getState(EnumStates.SANTA_CATARINA.getSigla());
        } else {
            uf = cliente.getState();
        }
        return EnumStates.SANTA_CATARINA.getSigla().equals(uf);
    }

    private boolean isParanaense() {
        Customer cliente = mCliente.getValue();
        if (cliente == null) {
            return false;
        }
        String uf;
        boolean inserindo = cliente.getId() == 0;
        if (inserindo) {
            uf = AppSettings.getState(EnumStates.SANTA_CATARINA.getSigla());
        } else {
            uf = cliente.getState();
        }
        return EnumStates.PARANA.getSigla().equals(uf);
    }

    public void validarCampos(Customer customer)
            throws CampoObrigatorioNaoInformadoException, MenorIdadeException {
        if (TextUtils.isEmpty(customer.getDocument())) {
            String cpf = getString(R.string.label_cpf);
            String mensagem = getString(R.string.campo_obrigatorio_nao_informado, cpf);
            throw new CampoObrigatorioNaoInformadoException(mensagem);
        }
        if (customer.getBirthdate() == null) {
            String dataNascimento = getString(R.string.label_data_nascimento);
            String mensagem = getString(R.string.campo_obrigatorio_nao_informado, dataNascimento);
            throw new CampoObrigatorioNaoInformadoException(mensagem);
        }
        if (isCatarinense() && TextUtils.isEmpty(customer.getDocumentoId())) {
            String rg = getString(R.string.label_rg);
            String mensagem = getString(R.string.campo_obrigatorio_nao_informado, rg);
            throw new CampoObrigatorioNaoInformadoException(mensagem);
        } else if (isParanaense() && isNotAdulto(customer.getBirthdate())) {
            String mensagem = getString(R.string.dados_invalidos_menor_de_idade);
            throw new MenorIdadeException(mensagem);
        }
    }

    public void validarCpf(String cpf) {
        AsyncValidationTask task = new AsyncValidationTask();
        this.mTask = task;
        task.execute(cpf);
    }

    @Override
    protected void onCleared() {
        if (hasAnyTaskRunning()) {
            mTask.cancel(true);
        }
        super.onCleared();
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncQueryTesk extends AsyncTask<Long, Void, Customer> {

        private String mErrorMessage = StringUtil.VAZIO;

        @Override
        protected Customer doInBackground(Long... ids) {
            Customer cliente = null;
            try {
                cliente = mDao.queryById(ids[0]);
            } catch (Exception e) {
                mErrorMessage = e.getMessage();
            }
            return cliente;
        }

        @Override
        protected void onPreExecute() {
            mProcessing.setValue(true);
        }

        @Override
        protected void onPostExecute(Customer cliente) {
            mProcessing.setValue(false);
            if (isSuccessful()) {
                mCliente.setValue(cliente);
            } else {
                String message = getString(R.string.erro_ao_consultar_cliente, mErrorMessage);
                ClienteViewModel.this.mErrorMessage.setValue(message);
            }
            super.onPostExecute(cliente);
        }

        @Override
        protected void onCancelled() {
            mProcessing.setValue(false);
        }

        private boolean isSuccessful() {
            return TextUtils.isEmpty(mErrorMessage);
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncValidationTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... cpfs) {
            return mDao.existsCpf(mId, cpfs[0]);
        }

        @Override
        protected void onPreExecute() {
            mProcessing.setValue(true);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean exists) {
            mProcessing.setValue(false);
            if (exists) {
                mErrorMessage.setValue(getString(R.string.cpf_ja_existe));
            }
            super.onPostExecute(exists);
        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            mProcessing.setValue(false);
            super.onCancelled(aBoolean);
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTransactionTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... args) {
            gravarAssincrono();
            return true;
        }

        @Override
        protected void onPreExecute() {
            mProcessing.setValue(true);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mProcessing.setValue(false);
            mEditando.setValue(false);
            mMessage.setValue(getString(R.string.cadastro_gravado));
            super.onPostExecute(success);
        }

        @Override
        protected void onCancelled() {
            mProcessing.setValue(false);
            mEditando.setValue(false);
            super.onCancelled();
        }

    }

}
