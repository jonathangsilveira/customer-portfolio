package com.portfolio.jgsilveira.customersportfolio.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.portfolio.jgsilveira.customersportfolio.R;
import com.portfolio.jgsilveira.customersportfolio.dao.ClienteDao;
import com.portfolio.jgsilveira.customersportfolio.model.Cliente;
import com.portfolio.jgsilveira.customersportfolio.settings.AppSettings;
import com.portfolio.jgsilveira.customersportfolio.settings.EnumEstados;
import com.portfolio.jgsilveira.customersportfolio.util.DateUtil;
import com.portfolio.jgsilveira.customersportfolio.util.StringUtil;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.Callable;

public class ClienteViewModel extends AppViewModel {

    private ClienteDao mDao;

    private MutableLiveData<Cliente> mCliente;

    private long mId;

    private MutableLiveData<Boolean> mEditando = new MutableLiveData<>();

    public ClienteViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    private void init() {
        mDao = getDatabase().clienteDao();
    }

    public LiveData<Cliente> getCliente(long id) {
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
        mCliente.postValue(new Cliente());
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
        Cliente cliente = mCliente.getValue();
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
        int idade = DateUtil.calcularIdade(dataNascimento, new Date());
        return idade < 18;
    }

    private void inserir(final Cliente cliente) {
        Objects.requireNonNull(cliente).setDataHoraCadastro(new Date());
        String uf = AppSettings.getState(EnumEstados.SANTA_CATARINA.getSigla());
        Objects.requireNonNull(cliente).setUf(uf);
        mId = getDatabase().runInTransaction(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return mDao.insert(cliente);
            }
        });
    }

    private void atualizar(final Cliente cliente) {
        getDatabase().runInTransaction(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return mDao.update(cliente);
            }
        });
    }

    public void gravar(@NonNull Cliente cliente) {
        atualizarValores(cliente);
        AsyncTransactionTask task = new AsyncTransactionTask();
        mTask = task;
        task.execute();
    }

    private void atualizarValores(@NonNull Cliente cliente) {
        Cliente valor = mCliente.getValue();
        if (valor != null) {
            valor.setNome(cliente.getNome());
            valor.setCpf(cliente.getCpf());
            valor.setDataNascimento(cliente.getDataNascimento());
            valor.setRg(cliente.getRg());
            valor.setTelefone(cliente.getTelefone());
        }
    }

    private void gravarAssincrono() {
        Cliente cliente = mCliente.getValue();
        boolean inserir = Objects.requireNonNull(cliente).getId() == 0;
        if (inserir) {
            inserir(cliente);
        } else {
            atualizar(cliente);
        }
    }

    private boolean isCatarinense() {
        Cliente cliente = mCliente.getValue();
        if (cliente == null) {
            return false;
        }
        String uf;
        if (TextUtils.isEmpty(cliente.getUf())) {
            uf = AppSettings.getState(EnumEstados.SANTA_CATARINA.getSigla());
        } else {
            uf = cliente.getUf();
        }
        return EnumEstados.SANTA_CATARINA.getSigla().equals(uf);
    }

    private boolean isParanaense() {
        Cliente cliente = mCliente.getValue();
        if (cliente == null) {
            return false;
        }
        String uf;
        boolean inserindo = cliente.getId() == 0;
        if (inserindo) {
            uf = AppSettings.getState(EnumEstados.SANTA_CATARINA.getSigla());
        } else {
            uf = cliente.getUf();
        }
        return EnumEstados.PARANA.getSigla().equals(uf);
    }

    public void validarCampos(Cliente cliente)
            throws CampoObrigatorioNaoInformadoException, MenorIdadeException {
        if (TextUtils.isEmpty(cliente.getCpf())) {
            String cpf = getString(R.string.label_cpf);
            String mensagem = getString(R.string.campo_obrigatorio_nao_informado, cpf);
            throw new CampoObrigatorioNaoInformadoException(mensagem);
        }
        if (cliente.getDataNascimento() == null) {
            String dataNascimento = getString(R.string.label_data_nascimento);
            String mensagem = getString(R.string.campo_obrigatorio_nao_informado, dataNascimento);
            throw new CampoObrigatorioNaoInformadoException(mensagem);
        }
        if (isCatarinense() && TextUtils.isEmpty(cliente.getRg())) {
            String rg = getString(R.string.label_rg);
            String mensagem = getString(R.string.campo_obrigatorio_nao_informado, rg);
            throw new CampoObrigatorioNaoInformadoException(mensagem);
        } else if (isParanaense() && isNotAdulto(cliente.getDataNascimento())) {
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
    private class AsyncQueryTesk extends AsyncTask<Long, Void, Cliente> {

        private String mErrorMessage = StringUtil.VAZIO;

        @Override
        protected Cliente doInBackground(Long... ids) {
            Cliente cliente = null;
            try {
                cliente = mDao.queryById(ids[0]);
            } catch (Exception e) {
                mErrorMessage = e.getMessage();
            }
            return cliente;
        }

        @Override
        protected void onPreExecute() {
            mProcessando.setValue(true);
        }

        @Override
        protected void onPostExecute(Cliente cliente) {
            mProcessando.setValue(false);
            if (isSuccessful()) {
                mCliente.setValue(cliente);
            } else {
                String message = getString(R.string.erro_ao_consultar_cliente, mErrorMessage);
                mMensagemErro.setValue(message);
            }
            super.onPostExecute(cliente);
        }

        @Override
        protected void onCancelled() {
            mProcessando.setValue(false);
        }

        private boolean isSuccessful() {
            return TextUtils.isEmpty(mErrorMessage);
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncValidationTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... cpfs) {
            return mDao.existsCpf(cpfs[0]);
        }

        @Override
        protected void onPreExecute() {
            mProcessando.setValue(true);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean exists) {
            mProcessando.setValue(false);
            if (exists) {
                mMensagemErro.setValue(getString(R.string.cpf_ja_existe));
            }
            super.onPostExecute(exists);
        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            mProcessando.setValue(false);
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
            mProcessando.setValue(true);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mProcessando.setValue(false);
            mEditando.setValue(false);
            mMensagem.setValue(getString(R.string.cadastro_gravado));
            super.onPostExecute(success);
        }

        @Override
        protected void onCancelled() {
            mProcessando.setValue(false);
            mEditando.setValue(false);
            super.onCancelled();
        }

    }

}
