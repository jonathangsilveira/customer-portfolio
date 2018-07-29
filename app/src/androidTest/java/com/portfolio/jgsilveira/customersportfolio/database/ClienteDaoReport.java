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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ClienteDaoReport {

    private AppDatabaseFactory mDatabase;

    @Before
    public void init() throws Exception {
        mDatabase = AppDatabase.getInMemoryDatabase(InstrumentationRegistry.getContext());
        prepareDatabase();
    }

    @Test
    public void queryWithParameters() {
        CustomerDao dao = mDatabase.clienteDao();
        String name = "ics";

        List<Customer> clientes = dao.queryReport(name, null, null, null, null, null, null);

        Assert.assertFalse(clientes.isEmpty());
    }

    @After
    public void clear() {
        mDatabase.close();
    }

    private List<Customer> clientList() {
        List<Customer> clientes = new ArrayList<>();
        Customer cliente = new Customer();
        cliente.setName("Pedro");
        cliente.setDocument("23136701046");
        cliente.setState(EnumStates.SANTA_CATARINA.getSigla());
        cliente.setTelephone("");
        cliente.setBirthdate(DateUtil.createDate(1990, Calendar.JANUARY, 15));
        cliente.setRegisterDate(DateUtil.createDate(2008, Calendar.FEBRUARY, 19, 15, 52, 0));
        clientes.add(cliente);
        cliente = new Customer();
        cliente.setName("Ericson");
        cliente.setDocument("98862684096");
        cliente.setState(EnumStates.SANTA_CATARINA.getSigla());
        cliente.setTelephone("");
        cliente.setBirthdate(DateUtil.createDate(1992, Calendar.DECEMBER, 8));
        cliente.setRegisterDate(DateUtil.createDate(2011, Calendar.JULY, 23, 9, 13, 0));
        clientes.add(cliente);
        cliente = new Customer();
        cliente.setName("Snake Sanders");
        cliente.setDocument("49673822077");
        cliente.setState(EnumStates.PARANA.getSigla());
        cliente.setTelephone("");
        cliente.setBirthdate(DateUtil.createDate(1995, Calendar.DECEMBER, 8));
        cliente.setRegisterDate(DateUtil.createDate(2016, Calendar.MARCH, 23, 10, 33, 0));
        clientes.add(cliente);
        return clientes;
    }

    private void prepareDatabase() throws Exception {
        CustomerDao dao = mDatabase.clienteDao();
        List<Customer> clientes = clientList();
        for (Customer cliente : clientes) {
            dao.insert(cliente);
        }
    }

}
