package com.grandi.lorenzo.gymtracker.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarHandler {

    private final Calendar calendar;

    public CalendarHandler() {
        this.calendar = Calendar.getInstance();;
    }
    public String getDate() {
        return new SimpleDateFormat("EEE, MMM d", Locale.UK).format(this.calendar.getTime());
    }
    public String getDateComplete(){
        return new SimpleDateFormat("EEE, MMM d - HH:mm:ss", Locale.UK).format(this.calendar.getTime());
    }
}
