package com.example.localcooking_v3t;

public class CalendarDay {
    private int day;
    private int month;
    private int year;
    private boolean isCurrentMonth;
    private boolean isSunday;
    private boolean isPast;

    public CalendarDay(int day, int month, int year, boolean isCurrentMonth, boolean isSunday, boolean isPast) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.isCurrentMonth = isCurrentMonth;
        this.isSunday = isSunday;
        this.isPast = isPast;
    }

    public int getDay() { return day; }
    public int getMonth() { return month; }
    public int getYear() { return year; }
    public boolean isCurrentMonth() { return isCurrentMonth; }
    public boolean isSunday() { return isSunday; }
    public boolean isPast() { return isPast; }
}