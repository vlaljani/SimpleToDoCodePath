package com.codepath.simpletodo;

import java.util.Calendar;

/**
 * Created by vibhalaljani on 12/29/14.
 */
public class MyCustomDate {
    private int dayOfMonth;
    private int monthOfYear;
    private int year;

    public MyCustomDate() {
        Calendar cal=Calendar.getInstance();

        year = cal.get(Calendar.YEAR);
        monthOfYear = cal.get(Calendar.MONTH);
        dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
    }

    public MyCustomDate(int day, int month, int year) {
        dayOfMonth = day;
        monthOfYear = month;
        this.year = year;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public int getMonthOfYear() {
        return monthOfYear;
    }

    public int getYear() {
        return year;
    }

    public void setDayOfMonth(int day) {
        dayOfMonth = day;
    }

    public void setMonthOfYear(int month) {
        monthOfYear = month;
    }

    public void setYear(int year){
        this.year = year;
    }
}
