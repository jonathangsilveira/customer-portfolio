package com.portfolio.jgsilveira.customersportfolio.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.Nullable;

import com.portfolio.jgsilveira.customersportfolio.model.Customer;
import com.portfolio.jgsilveira.customersportfolio.util.Converters;

import java.util.Date;
import java.util.List;

@Dao
@TypeConverters(Converters.class)
public interface CustomerDao extends BaseDao<Customer> {

    @Query("SELECT * FROM Customer")
    List<Customer> queryAll();

    @Query("SELECT * FROM Customer " +
            "WHERE (:name IS NULL OR LOWER(name) LIKE LOWER('%' || :name || '%')) " +
            "AND (:bornedFrom IS NULL OR birthdate >= :bornedFrom) " +
            "AND (:bornedTo IS NULL OR birthdate <= :bornedTo) " +
            "AND (:startDate IS NULL OR register_date >= :startDate) " +
            "AND (:endDate IS NULL OR register_date <= :endDate) " +
            "AND (:state IS NULL OR state = :state) " +
            "AND (:document IS NULL OR document LIKE '%' || :document || '%' OR " +
            "documentoId LIKE '%' || :document || '%')")
    List<Customer> queryReport(String name, Date bornedFrom, Date bornedTo, Date startDate,
                               Date endDate, String document, String state);

    @Query("SELECT * FROM Customer WHERE id = :id")
    @Nullable
    Customer queryById(long id);

    @Query("SELECT 1 FROM Customer WHERE document = :cpf AND id != :id ")
    boolean existsCpf(long id, String cpf);

}
