package com.portfolio.jgsilveira.customersportfolio;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.portfolio.jgsilveira.customersportfolio.model.Cliente;
import com.portfolio.jgsilveira.customersportfolio.settings.EnumEstados;
import com.portfolio.jgsilveira.customersportfolio.util.DateUtil;
import com.portfolio.jgsilveira.customersportfolio.util.DeviceUtil;
import com.portfolio.jgsilveira.customersportfolio.util.DialogUtil;
import com.portfolio.jgsilveira.customersportfolio.viewmodel.BusinessException;
import com.portfolio.jgsilveira.customersportfolio.viewmodel.CampoObrigatorioNaoInformadoException;
import com.portfolio.jgsilveira.customersportfolio.viewmodel.ClienteViewModel;
import com.portfolio.jgsilveira.customersportfolio.viewmodel.MenorIdadeException;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class ManterClienteActivity extends AppCompatActivity {

    private static final String ID_CLIENTE = "id";

    private ClienteViewModel mViewModel;

    private EditText mEditTextNome;

    private EditText mEditTextCpf;

    private EditText mEditTextNascimento;

    private EditText mEditTextRg;

    private EditText mEditTextTelefone;

    private ViewGroup mContainerProgressbar;

    private Button mButtonGravar;

    private ImageButton mImageButtonCalendario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manter_cliente);
        inicializarViews();
        inicializarListeners();
        int idCliente = getIntent().getIntExtra(ID_CLIENTE, 0);
        mViewModel = ViewModelProviders.of(this).get(ClienteViewModel.class);
        mViewModel.getCliente(idCliente).observe(this, new ClienteObserver());
        mViewModel.getProcessando().observe(this, new ProcessamentoObserver());
        mViewModel.getMensagem().observe(this, new MensagemObserver());
        //mudarPreferencia();
    }

    private void inicializarViews() {
        mEditTextNome = findViewById(R.id.activity_manter_cliente_nome);
        mEditTextCpf = findViewById(R.id.activity_manter_cliente_cpf);
        mEditTextNascimento = findViewById(R.id.activity_manter_cliente_nascimento);
        mEditTextRg = findViewById(R.id.activity_manter_cliente_rg);
        mEditTextTelefone = findViewById(R.id.activity_manter_cliente_telefone);
        mContainerProgressbar = findViewById(R.id.container_progressBar);
        mButtonGravar = findViewById(R.id.activity_manter_cliente_gravar);
        mImageButtonCalendario = findViewById(R.id.activity_manter_cliente_calendario);
    }

    private void inicializarListeners() {
        mButtonGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gravar();
            }
        });
        mImageButtonCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarCalendario();
            }
        });
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
            mEditTextTelefone.getText().clear();
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

    private void mudarPreferencia() {
        SharedPreferences preferences =
                getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("ESTADO_PADRAO", EnumEstados.PARANA.getSigla());
        edit.apply();
        edit.commit();
        Log.d("opa", preferences.getString("ESTADO_PADRAO",
                EnumEstados.SANTA_CATARINA.getSigla()));
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

}
