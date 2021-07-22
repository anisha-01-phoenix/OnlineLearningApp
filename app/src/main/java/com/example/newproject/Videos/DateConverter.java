package com.example.newproject.Videos;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
    static DateFormat format = new SimpleDateFormat("dd-mm-yyyy");

    @TypeConverter
    public static Date fromDate(String value) {
        if (value != null) {
            try {
                return format.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }
}