package com.portfolio.jgsilveira.customersportfolio;

import android.app.DatePickerDialog;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.portfolio.jgsilveira.customersportfolio.model.Cliente;
import com.portfolio.jgsilveira.customersportfolio.util.DateUtil;
import com.portfolio.jgsilveira.customersportfolio.util.DeviceUtil;
import com.portfolio.jgsilveira.customersportfolio.util.DialogUtil;
import com.portfolio.jgsilveira.customersportfolio.viewmodel.BusinessException;
import com.portfolio.jgsilveira.customersportfolio.viewmodel.ClienteViewModel;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ManterClienteActivity extends AppCompatActivity {

    private static final String ID_CLIENTE = "id";

    private ClienteViewModel mViewModel;

    private EditText mEditTextNome;

    private EditText mEditTextCpf;

    private EditText mEditTextNascimento;

    private EditText mEditTextRg;

    private EditText mEditTextTelefone;

    private ViewGroup mContainerProgressbar;

    private ImageButton mImageButtonCalendario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manter_cliente);
        inicializarToolbar();
        inicializarViews();
        inicializarListeners();
        long idCliente = getIntent().getLongExtra(ID_CLIENTE, 0);
        mViewModel = ViewModelProviders.of(this).get(ClienteViewModel.class);
        mViewModel.getCliente(idCliente).observe(this, new ClienteObserver());
        mViewModel.getProcessando().observe(this, new ProcessamentoObserver());
        mViewModel.getMensagem().observe(this, new MensagemObserver());
        mViewModel.getEditando().observe(this, new EdicaoObserver());
        mViewModel.getMensagemErro().observe(this, new ErroObserver());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manter_cliente_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                onAdicionar();
                break;
            case R.id.action_edit:
                onEditar();
                break;
            case R.id.action_save:
                gravar();
                break;
            case R.id.action_cancel:
                cancelarAlteracoes();
                break;
            case R.id.action_settings:
                configuracoes();
                break;
        }
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MutableLiveData<Boolean> editando = mViewModel.getEditando();
        Boolean isEditando = editando.getValue() != null && editando.getValue();
        if (isEditando) {
            menu.findItem(R.id.action_add).setVisible(false);
            menu.findItem(R.id.action_edit).setVisible(false);
            menu.findItem(R.id.action_save).setVisible(true);
            menu.findItem(R.id.action_cancel).setVisible(true);
        } else {
            menu.findItem(R.id.action_add).setVisible(true);
            menu.findItem(R.id.action_edit).setVisible(mViewModel.isEditando());
            menu.findItem(R.id.action_save).setVisible(false);
            menu.findItem(R.id.action_cancel).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void inicializarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.manter_cliente);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
    }

    private void inicializarViews() {
        mEditTextNome = findViewById(R.id.activity_manter_cliente_nome);
        mEditTextCpf = findViewById(R.id.activity_manter_cliente_cpf);
        mEditTextNascimento = findViewById(R.id.activity_manter_cliente_nascimento);
        mEditTextRg = findViewById(R.id.activity_manter_cliente_rg);
        mEditTextTelefone = findViewById(R.id.activity_manter_cliente_telefone);
        mContainerProgressbar = findViewById(R.id.container_progressBar);
        mImageButtonCalendario = findViewById(R.id.activity_manter_cliente_calendario);
    }

    private void inicializarListeners() {
        mImageButtonCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarCalendario();
            }
        });
        mEditTextCpf.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                String cpf = mEditTextCpf.getText().toString();
                if (TextUtils.isEmpty(cpf)) {
                    return;
                }
                if (!hasFocus) {
                    mViewModel.validarCpf(cpf);
                }
            }
        });
    }

    private void habilitarViews(boolean habilitar) {
        mEditTextNome.setEnabled(habilitar);
        mEditTextCpf.setEnabled(habilitar);
        mEditTextRg.setEnabled(habilitar);
        mEditTextTelefone.setEnabled(habilitar);
        mImageButtonCalendario.setEnabled(habilitar);
    }

    private void cancelarAlteracoes() {
        mViewModel.cancelarAlteracoes();
    }

    private void onEditar() {
        mViewModel.getEditando().setValue(true);
    }

    private void onAdicionar() {
        mViewModel.novoCliente();
    }

    private void configuracoes() {
        Intent intent = ConfiguracoesActivity.newIntent(this);
        startActivity(intent);
    }

    private void gravar() {
        try {
            DeviceUtil.hideKeyboard(this);
            Cliente cliente = getCliente();
            mViewModel.validarCampos(cliente);
            mViewModel.gravar(cliente);
        } catch (BusinessException e) {
            DialogUtil.showWarningDialog(this, e.getMessage());
        } catch (Exception e) {
            DialogUtil.showErrorDialog(this, e.getMessage());
        }
    }

    private Cliente getCliente() throws ParseException {
        Cliente cliente = new Cliente();
        cliente.setNome(mEditTextNome.getText().toString());
        cliente.setCpf(mEditTextCpf.getText().toString());
        if (mEditTextNascimento.getText().length() > 0) {
            cliente.setDataNascimento(
                    DateUtil.converterParaData(mEditTextNascimento.getText().toString()));
        }
        cliente.setRg(mEditTextRg.getText().toString());
        cliente.setTelefone(mEditTextTelefone.getText().toString());
        return cliente;
    }

    private void exibirCliente(@Nullable Cliente cliente) {
        if (cliente != null) {
            mEditTextNome.setText(cliente.getNome());
            mEditTextCpf.setText(cliente.getCpf());
            if (cliente.getDataNascimento() == null) {
                mEditTextNascimento.getText().clear();
            } else {
                mEditTextNascimento.setText(DateUtil.formatarData(cliente.getDataNascimento()));
            }
            mEditTextRg.setText(cliente.getRg());
            mEditTextTelefone.setText(cliente.getTelefone());
        }
    }

    private void apresentarMensagem(String mensagem) {
        View view = findViewById(R.id.activity_manter_cliente);
        Snackbar.make(view, mensagem, Snackbar.LENGTH_SHORT).show();
    }

    private void mostrarCalendario() {
        Calendar dataAtual = Calendar.getInstance();
        dataAtual.setTime(new Date());
        int ano = dataAtual.get(Calendar.YEAR);
        int mes = dataAtual.get(Calendar.MONTH);
        int dia = dataAtual.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(Calendar.YEAR, year);
                newDate.set(Calendar.MONTH, month);
                newDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mEditTextNascimento.setText(DateUtil.formatarData(newDate.getTime()));
            }
        }, ano, mes, dia);
        dialog.show();
    }

    public static Intent newIntent(Context context, long id) {
        Intent intent = new Intent(context, ManterClienteActivity.class);
        intent.putExtra(ID_CLIENTE, id);
        return intent;
    }

    private class MensagemObserver implements Observer<String> {

        @Override
        public void onChanged(@Nullable String mensagem) {
            if (TextUtils.isEmpty(mensagem)) {
                return;
            }
            apresentarMensagem(mensagem);
        }

    }

    private class ClienteObserver implements Observer<Cliente> {

        @Override
        public void onChanged(@Nullable Cliente cliente) {
            exibirCliente(cliente);
        }

    }

    private class ProcessamentoObserver implements Observer<Boolean> {

        @Override
        public void onChanged(@Nullable Boolean processing) {
            if (processing != null) {
                if (processing) {
                    mContainerProgressbar.setVisibility(View.VISIBLE);
                } else {
                    mContainerProgressbar.setVisibility(View.GONE);
                }
            }
        }

    }

    private class EdicaoObserver implements Observer<Boolean> {

        @Override
        public void onChanged(@Nullable Boolean editing) {
            supportInvalidateOptionsMenu();
            boolean isEditando = editing != null && editing;
            habilitarViews(isEditando);
        }

    }

    private class ErroObserver implements Observer<String> {

        @Override
        public void onChanged(@Nullable String message) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            DialogUtil.showWarningDialog(ManterClienteActivity.this, message);
        }

    }

}
