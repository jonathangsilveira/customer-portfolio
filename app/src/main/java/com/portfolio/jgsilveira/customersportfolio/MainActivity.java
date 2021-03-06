package com.portfolio.jgsilveira.customersportfolio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.portfolio.jgsilveira.customersportfolio.settings.AppSettings;

public class MainActivity extends AppCompatActivity {

    private Button mButtonCliente;

    private Button mButtonRelatorio;

    private Button mButtonConfiguracoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppSettings.getPreferences(getApplicationContext());
        initToolbar();
        initReferences();
        initListeners();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.customers_portfolio);
        setSupportActionBar(toolbar);
    }

    private void initReferences() {
        mButtonCliente = findViewById(R.id.activity_main_manter_cliente);
        mButtonRelatorio = findViewById(R.id.activity_main_relatorio);
        mButtonConfiguracoes = findViewById(R.id.activity_main_configuracoes);
    }

    private void initListeners() {
        mButtonCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onManterClienteClicked();
            }
        });
        mButtonRelatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRelatorioClicked();
            }
        });
        mButtonConfiguracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConfiguracoesClicked();
            }
        });
    }

    private void onManterClienteClicked() {
        Intent intent = ManterClienteActivity.newIntent(this, 0);
        startActivity(intent);
    }

    private void onRelatorioClicked() {
        startActivity(ReportActivity.newIntent(this));
    }

    private void onConfiguracoesClicked() {
        Intent intent = ConfiguracoesActivity.newIntent(this);
        startActivity(intent);
    }

}
