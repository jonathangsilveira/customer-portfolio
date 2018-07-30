package com.portfolio.jgsilveira.customersportfolio.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.portfolio.jgsilveira.customersportfolio.settings.AppSettings;
import com.portfolio.jgsilveira.customersportfolio.settings.EnumStates;

import java.util.Arrays;
import java.util.List;

public class ConfiguracoesViewModel extends AppViewModel {

    private MutableLiveData<List<EnumStates>> mEstados = new MutableLiveData<>();

    private MutableLiveData<Integer> mPosicaoEstado = new MutableLiveData<>();

    public ConfiguracoesViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    private void init() {
        String uf = AppSettings.getState(EnumStates.SANTA_CATARINA.getLowValue());
        EnumStates[] estados = EnumStates.values();
        mEstados.setValue(Arrays.asList(estados));
        for (int i = 0; i < estados.length; i++) {
            EnumStates estado = estados[i];
            if (!TextUtils.isEmpty(estado.getLowValue()) && estado.getLowValue().equals(uf)) {
                mPosicaoEstado.setValue(i);
            }
        }
    }

    public LiveData<List<EnumStates>> getEstados() {
        return mEstados;
    }

    public LiveData<Integer> getPosicaoEstado() {
        return mPosicaoEstado;
    }

}
