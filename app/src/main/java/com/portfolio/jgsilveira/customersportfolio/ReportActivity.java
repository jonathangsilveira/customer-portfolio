package com.portfolio.jgsilveira.customersportfolio;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.portfolio.jgsilveira.customersportfolio.util.DeviceUtil;
import com.portfolio.jgsilveira.customersportfolio.viewmodel.ReportViewModel;

import java.util.Objects;

public class ReportActivity extends AppCompatActivity {

    private ReportViewModel mViewModel;

    private ViewGroup mContainerProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        initToolbar();
        initReferences();
        mViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);
        mViewModel.getProcessing().observe(this, new ProcessObserver());
        mViewModel.getMessage().observe(this, new MessageObserver());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.report_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DeviceUtil.hideKeyboard(this);
        if (item.getItemId() == R.id.action_generate) {
            mViewModel.generateReport();
        } else if (item.getItemId() == R.id.action_clear) {
            mViewModel.clearFilters();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        showFilters();
        super.onResume();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, ReportActivity.class);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.relatorio);
        setSupportActionBar(toolbar);
        ActionBar actionBar = Objects.requireNonNull(getSupportActionBar());
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initReferences() {
        mContainerProgressbar = findViewById(R.id.container_progressBar);
    }

    private void showMessage(String mensagem) {
        View view = findViewById(R.id.activity_relatorio);
        Snackbar.make(view, mensagem, Snackbar.LENGTH_SHORT).show();
    }

    private void showFilters() {
        ReportFiltersFragment fragment = ReportFiltersFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_relatorio_container, fragment)
                .commit();
    }

    private class ProcessObserver implements Observer<Boolean> {

        @Override
        public void onChanged(@Nullable Boolean aBoolean) {
            boolean processing = aBoolean != null && aBoolean;
            if (processing) {
                mContainerProgressbar.setVisibility(View.VISIBLE);
            } else {
                mContainerProgressbar.setVisibility(View.GONE);
            }
        }

    }

    private class MessageObserver implements Observer<String> {

        @Override
        public void onChanged(@Nullable String message) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            showMessage(message);
        }

    }

}
