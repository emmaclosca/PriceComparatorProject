package com.pricecomparator.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateHelper {
    private static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate getDateFromString(String date) {
        return LocalDate.from(DATE_FORMAT.parse(date));
    }
}
