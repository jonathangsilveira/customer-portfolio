package com.portfolio.jgsilveira.customersportfolio.util;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat mDateTimeFormat =
            new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private DateUtil() {

    }

    public static int calculateAge(Date dataNascimento, Date dataAtual) {
        Calendar now = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();
        now.setTime(dataAtual);
        birthDate.setTime(dataNascimento);
        int currentYear = now.get(Calendar.YEAR);
        int birthYear = birthDate.get(Calendar.YEAR);
        int age = currentYear - birthYear;
        int currentMonth = now.get(Calendar.MONTH);
        int birthMonth = birthDate.get(Calendar.MONTH);
        if (birthMonth > currentMonth) {
            age--;
        } else if (currentMonth == birthMonth) {
            int currentDay = now.get(Calendar.DAY_OF_MONTH);
            int birthday = birthDate.get(Calendar.DAY_OF_MONTH);
            if (birthday > currentDay) {
                age--;
            }
        }
        return age;
    }

    public static String formatDateMedium(Date data) {
        return mDateFormat.format(data);
    }

    public static Date parseToDate(String valor) throws ParseException {
        return mDateFormat.parse(valor);
    }

    public static Date truncateDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date createDate(int year, int month, int dayOfMonth, int hour,
                                  int minutes, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, hour, minutes, seconds);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date createDate(int year, int month, int dayOfMonth) {
        return createDate(year, month, dayOfMonth, 0, 0, 0);
    }

    public static String formatDateTimeMedium(Date date) {
        return mDateTimeFormat.format(date);
    }

}
