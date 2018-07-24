package com.portfolio.jgsilveira.customersportfolio.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;
import android.database.SQLException;

import com.portfolio.jgsilveira.customersportfolio.util.Converters;

@Dao
@TypeConverters(Converters.class)
public interface BaseDao<T> {

    @Insert
    long insert(T model) throws Exception;

    @Delete
    int delete(T model) throws Exception;

    @Update
    int update(T model) throws Exception;

}
