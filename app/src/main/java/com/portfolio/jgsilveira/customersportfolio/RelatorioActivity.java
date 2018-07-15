package com.portfolio.jgsilveira.customersportfolio;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.portfolio.jgsilveira.customersportfolio.model.Cliente;
import com.portfolio.jgsilveira.customersportfolio.model.FiltroRelatorio;
import com.portfolio.jgsilveira.customersportfolio.util.DateUtil;
import com.portfolio.jgsilveira.customersportfolio.viewmodel.RelatorioViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RelatorioActivity extends AppCompatActivity {

    private EditText mEditTextNome;

    private EditText mEditTextNascimento;

    private ImageButton mImageButtonCalendarioNascimento;

    private EditText mEditTextCadastradoEm;

    private ImageButton mImageButtonCalendarioCadastradoEm;

    private EditText mEditTextCadastradoAte;

    private ImageButton mImageButtonCalendarioCadastradoAte;

    private DatePickerDialog mDatePickerNascimento;

    private TimePickerDialog mTimePickerCadastrado;

    private RelatorioViewModel mViewModel;

    private RecyclerView mRecyclerViewResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);
        initReferences();
        initListeners();
        setupRecyclerView();
        mViewModel = ViewModelProviders.of(this).get(RelatorioViewModel.class);
        mViewModel.getResultado().observe(this, new ResultadoObserver());
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewResultado.setLayoutManager(linearLayoutManager);

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
    }

    private void initListeners() {
        mEditTextNome.addTextChangedListener(new NomeTextWatcher());
        mImageButtonCalendarioNascimento.setOnClickListener(new OnCalendarioNascimentoClick());
        mImageButtonCalendarioCadastradoEm.setOnClickListener(new OnCalendarioCadastradoEmClick());
        mImageButtonCalendarioCadastradoAte
                .setOnClickListener(new OnCalendarioCadastradoAteClick());
    }

    private void mostrarCalendario(Date data, DatePickerDialog.OnDateSetListener listener) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int diaMes = calendar.get(Calendar.DAY_OF_MONTH);
        mDatePickerNascimento = new DatePickerDialog(this, listener, ano, mes, diaMes);
        mDatePickerNascimento.show();
    }

    private void mostrarHorario(Date data, TimePickerDialog.OnTimeSetListener listener) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
    }

    private class OnBirthdateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            FiltroRelatorio filtro = mViewModel.getFiltro().getValue();
            if (filtro != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                filtro.setDataNascimento(calendar.getTime());
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
                calendar.set(year, month, dayOfMonth);
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
                filtro.setDataFim(calendar.getTime());
                mEditTextCadastradoEm.setText(DateUtil.formatarData(filtro.getDataFim()));
            }
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
            FiltroRelatorio filtro = mViewModel.getFiltro().getValue();
            if (filtro != null) {
                mostrarCalendario(filtro.getDataNascimento(), new OnBirthdateSetListener());
            }
        }

    }

    private class OnCalendarioCadastradoEmClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            FiltroRelatorio filtro = mViewModel.getFiltro().getValue();
            if (filtro != null) {
                mostrarCalendario(filtro.getDataInicio(), new OnStartRecordDateSetListener());
            }
        }

    }

    private class OnCalendarioCadastradoAteClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            FiltroRelatorio filtro = mViewModel.getFiltro().getValue();
            if (filtro != null) {
                mostrarCalendario(filtro.getDataFim(), new OnEndRecordDateSetListener());
            }
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

    private class RelatorioAdapter extends RecyclerView.Adapter<RelatorioAdapter.ViewHolder> {

        private List<Cliente> mClientes = new ArrayList<>();

        RelatorioAdapter(List<Cliente> clientes) {
            mClientes.addAll(clientes);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int position) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View layout = inflater.inflate(R.layout.item_relatorio, parent);
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
