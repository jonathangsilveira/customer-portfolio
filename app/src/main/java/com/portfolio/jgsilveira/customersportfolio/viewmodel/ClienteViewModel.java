package com.portfolio.jgsilveira.customersportfolio.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.portfolio.jgsilveira.customersportfolio.R;
import com.portfolio.jgsilveira.customersportfolio.dao.ClienteDao;
import com.portfolio.jgsilveira.customersportfolio.database.AsyncDatabaseTransactionTask;
import com.portfolio.jgsilveira.customersportfolio.model.Cliente;
import com.portfolio.jgsilveira.customersportfolio.settings.AppSettings;
import com.portfolio.jgsilveira.customersportfolio.settings.EnumEstados;
import com.portfolio.jgsilveira.customersportfolio.util.DateUtil;

import java.util.Date;
import java.util.Objects;

public class ClienteViewModel extends AppViewModel {

    private ClienteDao mDao;

    private MutableLiveData<Cliente> mCliente;

    private AsyncDatabaseTransactionTask mTask;

    private long mId;

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
                mCliente.postValue(new Cliente());
            }
        }
        return mCliente;
    }

    private void atualizarCliente(long id) {
        mId = id;
        buscarCliente();
    }

    private void buscarCliente() {
        mTask = new AsyncDatabaseTransactionTask();
        mTask.registerCallback(new DatabaseQuery());
        mTask.execute();
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
        getDatabase().runInTransaction(new Runnable() {
            @Override
            public void run() {
                mDao.insert(cliente);
            }
        });
    }

    private void atualizar(final Cliente cliente) {
        getDatabase().runInTransaction(new Runnable() {
            @Override
            public void run() {
                mDao.update(cliente);
            }
        });
    }

    public void gravar(@NonNull Cliente cliente) {
        atualizarValores(cliente);
        mTask = new AsyncDatabaseTransactionTask();
        mTask.registerCallback(new DatabaseTransaction());
        mTask.execute();
    }

    private void atualizarValores(@NonNull Cliente cliente) {
        Cliente valor = mCliente.getValue();
        valor.setNome(cliente.getNome());
        valor.setCpf(cliente.getCpf());
        valor.setDataNascimento(cliente.getDataNascimento());
        valor.setRg(cliente.getRg());
    }

    private void gravarAsincrono() {
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

    private boolean hasAnyTaskRunning() {
        return mTask != null && !mTask.isCancelled();
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

    @Override
    protected void onCleared() {
        if (hasAnyTaskRunning()) {
            mTask.cancel(true);
        }
        super.onCleared();
    }

    private class DatabaseTransaction implements AsyncDatabaseTransactionTask.TransactionCallback {

        @Override
        public void onPreTransaction() {
            mProcessando.setValue(true);
        }

        @Override
        public boolean onTransaction() {
            gravarAsincrono();
            return true;
        }

        @Override
        public void onPostTransaction(boolean success, String message) {
            mProcessando.setValue(false);
            if (success) {
                mMensagem.setValue(getString(R.string.cadastro_gravado));
            }
        }

        @Override
        public void onCancelTransaction() {
            mProcessando.setValue(false);
        }

    }

    private class DatabaseQuery implements AsyncDatabaseTransactionTask.TransactionCallback {

        private Cliente mResult;

        @Override
        public void onPreTransaction() {
            mProcessando.setValue(true);
        }

        @Override
        public boolean onTransaction() {
            mResult = mDao.queryById(mId);
            return true;
        }

        @Override
        public void onPostTransaction(boolean success, String message) {
            mProcessando.setValue(false);
            if (success) {
                if (mResult != null) {
                    mCliente.setValue(mResult);
                }
            }
        }

        @Override
        public void onCancelTransaction() {
            mProcessando.setValue(false);
        }

    }

}
