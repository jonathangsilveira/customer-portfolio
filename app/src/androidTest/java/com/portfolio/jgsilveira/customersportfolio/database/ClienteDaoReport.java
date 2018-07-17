package com.portfolio.jgsilveira.customersportfolio.database;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.portfolio.jgsilveira.customersportfolio.dao.ClienteDao;
import com.portfolio.jgsilveira.customersportfolio.model.Cliente;
import com.portfolio.jgsilveira.customersportfolio.settings.EnumEstados;
import com.portfolio.jgsilveira.customersportfolio.util.DateUtil;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ClienteDaoReport {

    private AppDatabaseFactory mDatabase;

    @Before
    public void init() {
        mDatabase = AppDatabase.getInMemoryDatabase(InstrumentationRegistry.getContext());
        prepareDatabase();
    }

    @Test
    public void queryWithParameters() {
        ClienteDao dao = mDatabase.clienteDao();
        String name = "ics";

        List<Cliente> clientes = dao.queryWithParameters(name, null, null, null);

        Assert.assertFalse(clientes.isEmpty());
    }

    @After
    public void clear() {
        mDatabase.close();
    }

    private List<Cliente> clientList() {
        List<Cliente> clientes = new ArrayList<>();
        Cliente cliente = new Cliente();
        cliente.setNome("Pedro");
        cliente.setCpf("23136701046");
        cliente.setUf(EnumEstados.SANTA_CATARINA.getSigla());
        cliente.setTelefone("");
        cliente.setDataNascimento(DateUtil.createDate(1990, Calendar.JANUARY, 15));
        cliente.setDataHoraCadastro(DateUtil.createDate(2008, Calendar.FEBRUARY, 19, 15, 52, 0));
        clientes.add(cliente);
        cliente = new Cliente();
        cliente.setNome("Ericson");
        cliente.setCpf("98862684096");
        cliente.setUf(EnumEstados.SANTA_CATARINA.getSigla());
        cliente.setTelefone("");
        cliente.setDataNascimento(DateUtil.createDate(1992, Calendar.DECEMBER, 8));
        cliente.setDataHoraCadastro(DateUtil.createDate(2011, Calendar.JULY, 23, 9, 13, 0));
        clientes.add(cliente);
        cliente = new Cliente();
        cliente.setNome("Snake Sanders");
        cliente.setCpf("49673822077");
        cliente.setUf(EnumEstados.PARANA.getSigla());
        cliente.setTelefone("");
        cliente.setDataNascimento(DateUtil.createDate(1995, Calendar.DECEMBER, 8));
        cliente.setDataHoraCadastro(DateUtil.createDate(2016, Calendar.MARCH, 23, 10, 33, 0));
        clientes.add(cliente);
        return clientes;
    }

    private void prepareDatabase() {
        ClienteDao dao = mDatabase.clienteDao();
        List<Cliente> clientes = clientList();
        for (Cliente cliente : clientes) {
            dao.insert(cliente);
        }
    }

}
