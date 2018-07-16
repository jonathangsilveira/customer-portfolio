package com.portfolio.jgsilveira.customersportfolio.settings;

public enum EnumEstados {

    SANTA_CATARINA("SC", "Santa Catarina"), PARANA("PR", "Paraná");

    EnumEstados(String sigla, String descricao) {
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
