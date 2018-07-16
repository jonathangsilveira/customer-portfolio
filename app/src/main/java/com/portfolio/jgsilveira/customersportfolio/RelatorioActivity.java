package com.portfolio.jgsilveira.customersportfolio;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.portfolio.jgsilveira.customersportfolio.model.Cliente;
import com.portfolio.jgsilveira.customersportfolio.model.FiltroRelatorio;
import com.portfolio.jgsilveira.customersportfolio.util.DateUtil;
import com.portfolio.jgsilveira.customersportfolio.util.DeviceUtil;
import com.portfolio.jgsilveira.customersportfolio.util.DialogUtil;
import com.portfolio.jgsilveira.customersportfolio.viewmodel.RelatorioViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class RelatorioActivity extends AppCompatActivity {

    private EditText mEditTextNome;

    private EditText mEditTextNascimento;

    private ImageButton mImageButtonCalendarioNascimento;

    private EditText mEditTextCadastradoEm;

    private ImageButton mImageButtonCalendarioCadastradoEm;

    private EditText mEditTextCadastradoAte;

    private ImageButton mImageButtonCalendarioCadastradoAte;

    private RelatorioViewModel mViewModel;

    private RecyclerView mRecyclerViewResultado;

    private ViewGroup mContainerProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);
        initToolbar();
        initReferences();
        initListeners();
        setupRecyclerView();
        mViewModel = ViewModelProviders.of(this).get(RelatorioViewModel.class);
        mViewModel.getResultado().observe(this, new ResultadoObserver());
        mViewModel.getProcessando().observe(this, new ProcessamentoObserver());
        mViewModel.getMensagem().observe(this, new MensagemObserver());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.relatorio_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_gerar) {
            DeviceUtil.hideKeyboard(this);
            mViewModel.gerarRelatorio();
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, RelatorioActivity.class);
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewResultado.setLayoutManager(linearLayoutManager);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.relatorio);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
    }

    private void initReferences() {
        mEditTextNome = findViewById(R.id.activity_relatorio_nome);
        mEditTextNascimento = findViewById(R.id.activity_relatorio_nascimento);
        mEditTextCadastradoEm = findViewById(R.id.activity_relatorio_cadastrado_em);
        mEditTextCadastradoAte = findViewById(R.id.activity_relatorio_cadastrado_ate);
        mImageButtonCalendarioNascimento =
                findViewById(R.id.activity_relatorio_calendario_nascimento);
        mImageButtonCalendarioCadastradoEm =
                findViewById(R.id.activity_relatorio_calendario_cadastrado_em);
        mImageButtonCalendarioCadastradoAte =
                findViewById(R.id.activity_relatorio_calendario_cadastrado_ate);
        mRecyclerViewResultado = findViewById(R.id.activity_relatorio_resultados);
        mContainerProgressbar = findViewById(R.id.container_progressBar);
    }

    private void initListeners() {
        mEditTextNome.addTextChangedListener(new NomeTextWatcher());
        mImageButtonCalendarioNascimento.setOnClickListener(new OnCalendarioNascimentoClick());
        mImageButtonCalendarioCadastradoEm.setOnClickListener(new OnCalendarioCadastradoEmClick());
        mImageButtonCalendarioCadastradoAte
                .setOnClickListener(new OnCalendarioCadastradoAteClick());
    }

    private void mostrarCalendarioAniversario() {
        FiltroRelatorio filtro = mViewModel.getFiltro().getValue();
        if (filtro != null) {
            Date dataNascimento;
            if (filtro.getDataNascimento() == null) {
                dataNascimento = new Date();
            } else {
                dataNascimento = filtro.getDataNascimento();
            }
            DialogUtil.showDatePickerDialog(this, dataNascimento,
                    new OnBirthdateSetListener(), new OnClearBirthDateListener());
        }
    }

    private void mostrarCalendarioInicio() {
        FiltroRelatorio filtro = mViewModel.getFiltro().getValue();
        if (filtro != null) {
            Date dataInicio;
            if (filtro.getDataInicio() == null) {
                dataInicio = new Date();
            } else {
                dataInicio = filtro.getDataInicio();
            }
            DialogUtil.showDatePickerDialog(this, dataInicio,
                    new OnStartRecordDateSetListener(), new OnClearStartRecordDateListener());
        }
    }

    private void mostrarCalendarioFim() {
        FiltroRelatorio filtro = mViewModel.getFiltro().getValue();
        if (filtro != null) {
            Date dataInicio;
            if (filtro.getDataFim() == null) {
                dataInicio = new Date();
            } else {
                dataInicio = filtro.getDataFim();
            }
            DialogUtil.showDatePickerDialog(this, dataInicio,
                    new OnEndRecordDateSetListener(), new OnClearEndRecordDateListener());
        }
    }

    private void apresentarMensagem(String mensagem) {
        View view = findViewById(R.id.activity_relatorio);
        Snackbar.make(view, mensagem, Snackbar.LENGTH_SHORT).show();
    }

    private class OnBirthdateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            FiltroRelatorio filtro = mViewModel.getFiltro().getValue();
            if (filtro != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                filtro.setDataNascimento(DateUtil.truncateDate(calendar.getTime()));
                mEditTextNascimento.setText(DateUtil.formatarData(filtro.getDataNascimento()));
            }
        }

    }

    private class OnStartRecordDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            FiltroRelatorio filtro = mViewModel.getFiltro().getValue();
            if (filtro != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth, 23, 59, 59);
                filtro.setDataInicio(calendar.getTime());
                mEditTextCadastradoEm.setText(DateUtil.formatarData(filtro.getDataInicio()));
            }
        }

    }

    private class OnEndRecordDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            FiltroRelatorio filtro = mViewModel.getFiltro().getValue();
            if (filtro != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                filtro.setDataFim(DateUtil.truncateDate(calendar.getTime()));
                mEditTextCadastradoAte.setText(DateUtil.formatarData(filtro.getDataFim()));
            }
        }

    }

    private class OnClearBirthDateListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            mEditTextNascimento.getText().clear();
            FiltroRelatorio filtro = mViewModel.getFiltro().getValue();
            if (filtro != null) {
                filtro.setDataNascimento(null);
            }
            dialogInterface.dismiss();
        }

    }

    private class OnClearStartRecordDateListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            mEditTextCadastradoEm.getText().clear();
            FiltroRelatorio filtro = mViewModel.getFiltro().getValue();
            if (filtro != null) {
                filtro.setDataInicio(null);
            }
            dialogInterface.dismiss();
        }

    }

    private class OnClearEndRecordDateListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            mEditTextCadastradoAte.getText().clear();
            FiltroRelatorio filtro = mViewModel.getFiltro().getValue();
            if (filtro != null) {
                filtro.setDataFim(null);
            }
            dialogInterface.dismiss();
        }

    }

    private class NomeTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence text, int i, int i1, int i2) {
            FiltroRelatorio filtro = mViewModel.getFiltro().getValue();
            if (filtro != null) {
                filtro.setNome(text.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

    }

    private class OnCalendarioNascimentoClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            mostrarCalendarioAniversario();
        }

    }

    private class OnCalendarioCadastradoEmClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            mostrarCalendarioInicio();
        }

    }

    private class OnCalendarioCadastradoAteClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            mostrarCalendarioFim();
        }

    }

    private class ResultadoObserver implements Observer<List<Cliente>> {

        @Override
        public void onChanged(@Nullable List<Cliente> clientes) {
            if (clientes == null) {
                mRecyclerViewResultado.setAdapter(new RelatorioAdapter(new ArrayList<Cliente>()));
            } else {
                mRecyclerViewResultado.setAdapter(new RelatorioAdapter(clientes));
            }
        }

    }

    private class ProcessamentoObserver implements Observer<Boolean> {

        @Override
        public void onChanged(@Nullable Boolean processing) {
            if (processing != null && processing) {
                mContainerProgressbar.setVisibility(View.VISIBLE);
            } else {
                mContainerProgressbar.setVisibility(View.GONE);
            }
        }

    }

    private class MensagemObserver implements Observer<String> {

        @Override
        public void onChanged(@Nullable String message) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            apresentarMensagem(message);
        }

    }

    private class RelatorioAdapter extends RecyclerView.Adapter<RelatorioAdapter.ViewHolder> {

        private List<Cliente> mClientes = new ArrayList<>();

        RelatorioAdapter(List<Cliente> clientes) {
            mClientes.addAll(clientes);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int position) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View layout = inflater.inflate(R.layout.item_relatorio, parent, false);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = ManterClienteActivity
                            .newIntent(parent.getContext(), getItemId(position));
                    startActivity(intent);
                }
            });
            return new RelatorioAdapter.ViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            Cliente cliente = mClientes.get(position);
            viewHolder.mTextViewNome.setText(cliente.getNome());
            viewHolder.mTextViewCadastrado
                    .setText(DateUtil.formatarData(cliente.getDataHoraCadastro()));
            viewHolder.mTextViewUf.setText(cliente.getUf());
        }

        @Override
        public int getItemCount() {
            return mClientes.size();
        }

        @Override
        public long getItemId(int position) {
            return mClientes.get(position).getId();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView mTextViewNome;

            private TextView mTextViewCadastrado;

            private TextView mTextViewUf;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                init(itemView);
            }

            private void init(@NonNull View itemView) {
                mTextViewNome = itemView.findViewById(R.id.item_relatorio_nome);
                mTextViewCadastrado = itemView.findViewById(R.id.item_relatorio_cadastrado);
                mTextViewUf = itemView.findViewById(R.id.item_relatorio_uf);
            }

        }

    }

}
