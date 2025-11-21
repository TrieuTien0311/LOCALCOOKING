package com.example.localcooking_v3t;

import java.util.List;

public class CalendarMonth {
    private int year;
    private int month;
    private List<CalendarDay> days;

    public CalendarMonth(int year, int month, List<CalendarDay> days) {
        this.year = year;
        this.month = month;
        this.days = days;
    }

    public int getYear() { return year; }
    public int getMonth() { return month; }
    public List<CalendarDay> getDays() { return days; }
}
