package com.portfolio.jgsilveira.customersportfolio.settings;

import com.portfolio.jgsilveira.customersportfolio.util.StringUtil;

public enum EnumStates {

    EMPTY(StringUtil.VAZIO, StringUtil.VAZIO),
    SANTA_CATARINA("SC", "Santa Catarina"),
    PARANA("PR", "Paraná");

    EnumStates(String sigla, String descricao) {
        mSigla = sigla;
        mDescricao = descricao;
    }

    private String mSigla;

    private String mDescricao;

    public String getSigla() {
        return mSigla;
    }

    public String getDescricao() {
        return mDescricao;
    }

    @Override
    public String toString() {
        return mDescricao;
    }

}
