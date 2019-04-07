package com.wtillett.c196project.database;

import android.arch.persistence.room.TypeConverter;

import java.time.LocalDate;

public class DateConverter {
    @TypeConverter
    public static LocalDate toDate(String string) {
        return string == null ? null : LocalDate.parse(string);
    }

    @TypeConverter
    public static String toString(LocalDate date) {
        return date == null ? null : date.toString();
    }
}
