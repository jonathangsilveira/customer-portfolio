package com.portfolio.jgsilveira.customersportfolio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
        initReferences();
        initListeners();
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

            }
        });
    }

    private void onManterClienteClicked() {
        Intent intent = ManterClienteActivity.newIntent(this, 0);
        startActivity(intent);
    }

    private void onRelatorioClicked() {
        //TODO
    }

    private void onConfiguracoesClicked() {
        //TODO
    }

}
