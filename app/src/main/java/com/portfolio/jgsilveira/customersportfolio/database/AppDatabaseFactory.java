package com.portfolio.jgsilveira.customersportfolio.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.portfolio.jgsilveira.customersportfolio.dao.CustomerDao;
import com.portfolio.jgsilveira.customersportfolio.model.Customer;

@Database(entities = {Customer.class}, version = 1, exportSchema = false)
public abstract class AppDatabaseFactory extends RoomDatabase {

    public abstract CustomerDao clienteDao();

}
