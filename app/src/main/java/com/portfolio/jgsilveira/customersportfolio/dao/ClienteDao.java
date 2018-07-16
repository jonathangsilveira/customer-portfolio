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

    @Query("SELECT * FROM Cliente " +
            "WHERE (IFNULL(:name, '') = '' OR LOWER(nome) LIKE LOWER('%:name%')) " +
            "AND (:birthDate IS NULL OR data_nascimento = :birthDate) " +
            "AND (:startDate IS NULL OR data_hora_cadastro >= :startDate) " +
            "AND (:endDate IS NULL OR data_hora_cadastro <= :endDate)")
    List<Cliente> queryWithParameters(String name, Date birthDate, Date startDate, Date endDate);

    @Query("SELECT * FROM Cliente WHERE id = :id")
    @Nullable
    Cliente queryById(long id);

    @Query("SELECT 1 FROM Cliente WHERE cpf = :cpf")
    boolean existsCpf(String cpf);

}
