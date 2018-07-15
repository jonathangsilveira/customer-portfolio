package com.portfolio.jgsilveira.customersportfolio.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.portfolio.jgsilveira.customersportfolio.util.Converters;

@Dao
@TypeConverters(Converters.class)
public interface BaseDao<T> {

    @Insert
    long insert(T model);

    @Delete
    int delete(T model);

    @Update
    int update(T model);

}
