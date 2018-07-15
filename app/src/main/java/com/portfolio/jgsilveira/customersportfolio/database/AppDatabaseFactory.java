package com.portfolio.jgsilveira.customersportfolio.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.portfolio.jgsilveira.customersportfolio.dao.ClienteDao;
import com.portfolio.jgsilveira.customersportfolio.model.Cliente;

@Database(entities = {Cliente.class}, version = 1)
public abstract class AppDatabaseFactory extends RoomDatabase {

    public abstract ClienteDao clienteDao();

}
