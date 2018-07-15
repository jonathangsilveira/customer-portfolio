package com.portfolio.jgsilveira.customersportfolio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.portfolio.jgsilveira.customersportfolio.settings.AppSettings;
import com.portfolio.jgsilveira.customersportfolio.settings.EnumEstados;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppSettings.getPreferences(getApplicationContext());
        Intent intent = ManterClienteActivity.newIntent(this, 1);
        startActivity(intent);
    }

}
