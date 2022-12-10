package com.mpl.weather;

import androidx.room.Entity;

@Entity
public class Statistics {

    public String clothName;
    public int wearCount;

    Statistics(String clothName, int wearCount) {
        this.clothName = clothName;
        this.wearCount = wearCount;
    }
}