package com.example.localcooking_v3t;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarGenerator {

    public static List<CalendarMonth> generateMonths(int startMonth, int startYear, int count) {
        List<CalendarMonth> months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(startYear, startMonth, 1);

        for (int i = 0; i < count; i++) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            List<CalendarDay> days = generateDaysForMonth(year, month);

            months.add(new CalendarMonth(year, month, days));
            calendar.add(Calendar.MONTH, 1);
        }

        return months;
    }

    private static List<CalendarDay> generateDaysForMonth(int year, int month) {
        List<CalendarDay> days = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);

        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // Previous month days
        Calendar prevMonthCalendar = (Calendar) calendar.clone();
        prevMonthCalendar.add(Calendar.MONTH, -1);
        int prevMonthMaxDay = prevMonthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int prevMonth = prevMonthCalendar.get(Calendar.MONTH);
        int prevYear = prevMonthCalendar.get(Calendar.YEAR);

        for (int i = firstDayOfWeek - 1; i >= 0; i--) {
            int day = prevMonthMaxDay - i;
            days.add(new CalendarDay(day, prevMonth, prevYear, false, false));
        }

        // Current month days
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int day = 1; day <= maxDay; day++) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
            boolean isSunday = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
            days.add(new CalendarDay(day, month, year, true, isSunday));
        }

        // Next month days
        int remainingDays = 42 - days.size();
        Calendar nextMonthCalendar = Calendar.getInstance();
        nextMonthCalendar.set(year, month, 1);
        nextMonthCalendar.add(Calendar.MONTH, 1);
        int nextMonth = nextMonthCalendar.get(Calendar.MONTH);
        int nextYear = nextMonthCalendar.get(Calendar.YEAR);

        for (int day = 1; day <= remainingDays; day++) {
            days.add(new CalendarDay(day, nextMonth, nextYear, false, false));
        }

        return days;
    }
}