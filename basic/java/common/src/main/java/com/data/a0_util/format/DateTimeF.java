package com.data.a0_util.format;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class DateTimeF {
    private static final SimpleDateFormat time_format = new SimpleDateFormat("yyyy-MM-dd");

    public static Date parse(String date_value) {
        try {
            Date date = time_format.parse(date_value);
            return date;

        } catch (ParseException e) {
            log.error("parse date {} failed, exception: {} ",
                    date_value, e.getMessage());
            System.out.println("parse date " + date_value + " failed");
            return null;
        }
    }

    public static String format(Date date) {
        return time_format.format(date);
    }
}
