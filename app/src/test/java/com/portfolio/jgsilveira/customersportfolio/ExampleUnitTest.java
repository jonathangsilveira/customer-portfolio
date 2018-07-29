package com.portfolio.jgsilveira.customersportfolio;

import com.portfolio.jgsilveira.customersportfolio.util.DateUtil;

import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void show() throws ParseException {
        Date data = DateUtil.parseToDate("15/01/1990");
        System.out.println(DateUtil.formatDateMedium(data));
    }

}