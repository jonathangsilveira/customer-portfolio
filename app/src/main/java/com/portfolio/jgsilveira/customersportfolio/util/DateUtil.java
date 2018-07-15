package com.portfolio.jgsilveira.customersportfolio.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static Locale DEFAULT_LOCALE = new Locale("pt","BR");

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    private DateUtil() {

    }

    public static int calcularIdade(Date dataNascimento, Date dataAtual) {
        Calendar agora = Calendar.getInstance();
        Calendar nascimento = Calendar.getInstance();
        agora.setTime(dataAtual);
        nascimento.setTime(dataNascimento);
        int anoAtual = agora.get(Calendar.YEAR);
        int anoNascimento = nascimento.get(Calendar.YEAR);
        int idade = anoAtual - anoNascimento;
        int mesAtual = agora.get(Calendar.MONTH);
        int mesNascimento = nascimento.get(Calendar.MONTH);
        if (mesNascimento > mesAtual) {
            idade--;
        } else if (mesAtual == mesNascimento) {
            int diaAtual = agora.get(Calendar.DAY_OF_MONTH);
            int diaNascimento = nascimento.get(Calendar.DAY_OF_MONTH);
            if (diaNascimento > diaAtual) {
                idade--;
            }
        }
        return idade;
    }

    public static String formatarData(Date data) {
        return DateFormat.getDateInstance(DateFormat.MEDIUM, DEFAULT_LOCALE).format(data);
    }

    public static Date converterParaData(String valor) throws ParseException {
        DateFormat dateInstance = DateFormat.getDateInstance(DateFormat.MEDIUM, DEFAULT_LOCALE);
        return dateInstance.parse(valor);
    }

}
