package com.kolya.gym.data;

import com.kolya.gym.domain.Month;

public class YearMonth {
    private int year;
    private Month month;

    public YearMonth(int year, Month month) {
        this.year = year;
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }
}
