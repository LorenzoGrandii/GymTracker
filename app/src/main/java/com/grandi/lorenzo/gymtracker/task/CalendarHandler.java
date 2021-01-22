package com.grandi.lorenzo.gymtracker.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarHandler {

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    public CalendarHandler() {
        this.calendar = Calendar.getInstance();
        this.dateFormat = new SimpleDateFormat("EEE, MMM d", Locale.UK);
        this.date = dateFormat.format(calendar.getTime());
    }
    public String getDate() {
        return date;
    }
}
