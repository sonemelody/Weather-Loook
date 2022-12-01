package com.mpl.weather;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Statistics {
    public int temperature;
    @PrimaryKey
    public Cloth cloth;
    public int wearCount;

    Statistics(int temperature, Cloth cloth, int wearCount) {
        this.temperature = temperature;
        this.cloth = cloth;
        this.wearCount = wearCount;
    }
}