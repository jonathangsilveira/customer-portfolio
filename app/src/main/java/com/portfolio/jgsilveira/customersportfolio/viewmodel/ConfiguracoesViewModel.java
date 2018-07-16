package com.portfolio.jgsilveira.customersportfolio.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.portfolio.jgsilveira.customersportfolio.settings.AppSettings;
import com.portfolio.jgsilveira.customersportfolio.settings.EnumEstados;

import java.util.Arrays;
import java.util.List;

public class ConfiguracoesViewModel extends AppViewModel {

    private MutableLiveData<List<EnumEstados>> mEstados = new MutableLiveData<>();

    private MutableLiveData<Integer> mPosicaoEstado = new MutableLiveData<>();

    public ConfiguracoesViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    private void init() {
        String uf = AppSettings.getState(EnumEstados.SANTA_CATARINA.getSigla());
        EnumEstados[] estados = EnumEstados.values();
        mEstados.setValue(Arrays.asList(estados));
        for (int i = 0; i < estados.length; i++) {
            EnumEstados estado = estados[i];
            if (estado.getSigla().equals(uf)) {
                mPosicaoEstado.setValue(i);
            }
        }
    }

    public LiveData<List<EnumEstados>> getEstados() {
        return mEstados;
    }

    public LiveData<Integer> getPosicaoEstado() {
        return mPosicaoEstado;
    }

}
