package com.portfolio.jgsilveira.customersportfolio.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.portfolio.jgsilveira.customersportfolio.util.Converters;

import java.util.Date;

@Entity(indices = {@Index(value = {"cpf"}, unique = true)})
@TypeConverters(Converters.class)
public class Cliente {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String cpf;

    private String nome;

    private String rg;

    @ColumnInfo(name = "data_hora_cadastro")
    private Date dataHoraCadastro;

    @ColumnInfo(name = "data_nascimento")
    private Date dataNascimento;

    private String uf;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public Date getDataHoraCadastro() {
        return dataHoraCadastro;
    }

    public void setDataHoraCadastro(Date dataHoraCadastro) {
        this.dataHoraCadastro = dataHoraCadastro;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

}
