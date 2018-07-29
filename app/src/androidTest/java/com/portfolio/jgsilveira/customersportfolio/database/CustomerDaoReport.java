package com.portfolio.jgsilveira.customersportfolio.database;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.portfolio.jgsilveira.customersportfolio.dao.CustomerDao;
import com.portfolio.jgsilveira.customersportfolio.model.Customer;
import com.portfolio.jgsilveira.customersportfolio.settings.EnumStates;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class CustomerDaoReport {

    private AppDatabaseFactory mDatabase;

    @Before
    public void init() {
        mDatabase = AppDatabase.getInstance(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void queryReport_withResults() {
        CustomerDao dao = mDatabase.clienteDao();
        String name = "João";
        String document = "5";
        String state = EnumStates.SANTA_CATARINA.getSigla();

        List<Customer> customers = dao.queryReport(name, null, null, null, null, document, state);
        //List<Customer> customers = dao.queryAll();

        Assert.assertFalse(customers.isEmpty());
    }

    @After
    public void clear() {
        mDatabase.close();
    }

}
