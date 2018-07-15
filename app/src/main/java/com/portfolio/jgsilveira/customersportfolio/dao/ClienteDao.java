package com.portfolio.jgsilveira.customersportfolio.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.Nullable;

import com.portfolio.jgsilveira.customersportfolio.model.Cliente;
import com.portfolio.jgsilveira.customersportfolio.util.Converters;

import java.util.Date;
import java.util.List;

@Dao
@TypeConverters(Converters.class)
public interface ClienteDao extends BaseDao<Cliente> {

    @Query("SELECT * FROM Cliente")
    List<Cliente> queryAll();

    @Query("SELECT * FROM Cliente WHERE nome LIKE :nome AND data_nascimento = :dataNascimento AND " +
            "data_hora_cadastro >= :dataInicio AND data_hora_cadastro <= :dataFim")
    List<Cliente> queryWithParameters(String nome, Date dataNascimento, Date dataInicio,
                                      Date dataFim);

    @Query("SELECT * FROM Cliente WHERE id = :id")
    @Nullable
    Cliente queryById(long id);

}
