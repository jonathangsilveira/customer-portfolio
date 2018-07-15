package com.portfolio.jgsilveira.customersportfolio.util;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class Converters {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static boolean toBoolean(Integer bit) {
        return bit != null && bit == 1;
    }

    @TypeConverter
    public static int toBit(Boolean value) {
        return value != null && value ? 1 : 0;
    }

}
