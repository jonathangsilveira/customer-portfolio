package com.portfolio.jgsilveira.customersportfolio.database;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.portfolio.jgsilveira.customersportfolio.dao.CustomerDao;
import com.portfolio.jgsilveira.customersportfolio.model.Customer;
import com.portfolio.jgsilveira.customersportfolio.settings.EnumStates;
import com.portfolio.jgsilveira.customersportfolio.util.DateUtil;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;
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
        String name = null;//"João";
        String document = null;//"5";
        String state = EnumStates.EMPTY.getLowValue();
        Date startDate = DateUtil.createDate(2018, Calendar.JULY, 28, 0, 0, 0);
        Date endDate = DateUtil.createDate(2018, Calendar.JULY, 28, 23, 59, 59);

        List<Customer> customers = dao.queryReport(name, null, null, startDate, endDate, document, state);
        //List<Customer> customers = dao.queryAll();

        Assert.assertFalse(customers.isEmpty());
    }

    @After
    public void clear() {
        mDatabase.close();
    }

}
