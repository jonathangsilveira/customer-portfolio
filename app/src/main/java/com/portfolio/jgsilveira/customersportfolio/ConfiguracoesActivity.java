package com.portfolio.jgsilveira.customersportfolio;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.portfolio.jgsilveira.customersportfolio.settings.AppSettings;
import com.portfolio.jgsilveira.customersportfolio.settings.EnumStates;
import com.portfolio.jgsilveira.customersportfolio.viewmodel.ConfiguracoesViewModel;

import java.util.List;
import java.util.Objects;

public class ConfiguracoesActivity extends AppCompatActivity {

    private Spinner mSpinnerEstado;

    private ConfiguracoesViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        initToolbar();
        mSpinnerEstado = findViewById(R.id.activity_configuracoes_estado);
        mSpinnerEstado.setOnItemSelectedListener(new OnEstadoSelected());
        mViewModel = ViewModelProviders.of(this).get(ConfiguracoesViewModel.class);
        mViewModel.getEstados().observe(this, new EstadoObserver());
        mViewModel.getPosicaoEstado().observe(this, new PosicaoEstadoObserver());
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, ConfiguracoesActivity.class);
    }

    @NonNull
    private ArrayAdapter<EnumStates> newAdapter(List<EnumStates> values) {
        return new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                android.R.id.text1, values);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.configuracoes);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
    }

    private class EstadoObserver implements Observer<List<EnumStates>>{

        @Override
        public void onChanged(@Nullable List<EnumStates> estados) {
            if (estados != null) {
                mSpinnerEstado.setAdapter(newAdapter(estados));
            }
        }

    }

    private class PosicaoEstadoObserver implements Observer<Integer> {

        @Override
        public void onChanged(@Nullable Integer posicao) {
            if (posicao != null && posicao > Spinner.INVALID_POSITION) {
                mSpinnerEstado.setSelection(posicao);
            }
        }

    }

    private class OnEstadoSelected implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            List<EnumStates> estados = mViewModel.getEstados().getValue();
            if (estados != null) {
                EnumStates selecionado = estados.get(position);
                AppSettings.putState(selecionado.getLowValue());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }

}
